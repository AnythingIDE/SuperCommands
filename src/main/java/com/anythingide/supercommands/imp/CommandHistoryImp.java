package com.anythingide.supercommands.imp;

import java.util.List;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandExecuteResult;
import com.anythingide.supercommands.CommandHistory;
import com.anythingide.supercommands.CommandHistoryConfig;
import com.anythingide.supercommands.CommandInterceptorContext;
import com.anythingide.supercommands.CommandMultiExecuteStrategy;
import com.anythingide.supercommands.CommandStack;
import com.anythingide.supercommands.exception.CommandFailedException;

import reactor.core.publisher.Mono;

public class CommandHistoryImp<T extends Command<T>> implements CommandHistory<T> {

	private final List<CommandExecuteInterceptor<T>> interceptors;
	private final CommandExecuteInterceptor<T> finalInterceptor = new CommandExecutorInterceptor<T>();
	private final CommandStack<T> undoCommandStack = CommandStack.createDefault();
	private final CommandStack<T> redoCommandStack = CommandStack.createDefault();
	private final CommandMultiExecuteStrategy<T> multiUndoStrategy;
	private final CommandMultiExecuteStrategy<T> multiRedoStrategy;

	public CommandHistoryImp(CommandHistoryConfig<T> configs) {
		this.interceptors = configs.getInterceptors();
		this.multiUndoStrategy = configs.getMultiUndoStrategy();
		this.multiRedoStrategy = configs.getMultiRedoStrategy();
	}

	@Override
	public Publisher<Integer> undo(int numCommands) {
		return multiUndoStrategy.executeCommands(() -> undo(), numCommands);
	}

	@Override
	public Publisher<Integer> redo(int numCommands) {
		return multiRedoStrategy.executeCommands(() -> redo(), numCommands);
	}

	@Override
	public Publisher<Boolean> undo() {
		return executeCommandAndSwapStack(undoCommandStack, redoCommandStack);
	}

	@Override
	public Publisher<Boolean> redo() {
		return executeCommandAndSwapStack(redoCommandStack, undoCommandStack);
	}

	@Override
	public Publisher<Void> submit(T command) {
		return executeCommand(command)
			.flatMap(CommandHistoryImp::transformFailedExecutionResult)
			.map(result -> result.getUndoCommand())
			.flatMap(Mono::justOrEmpty)
			.doOnNext(_1 -> redoCommandStack.clear())
			.doOnNext(undoCommand -> undoCommandStack.push(undoCommand))
			.then();
	}
	
	private static <T extends Command<T>> Mono<CommandExecuteResult<T>> transformFailedExecutionResult(CommandExecuteResult<T> executionResult) {
		if (executionResult.wasSuccessful()) {
			return Mono.just(executionResult);
		}
		return Mono.error(new CommandFailedException(executionResult));
	}
	
	private Mono<CommandExecuteResult<T>> executeCommand(T command) {
		CommandInterceptorContext<T> context = createInterceptorContext(0);
		
		return Mono.from(context.next(command))
			.onErrorMap(e -> new CommandFailedException(CommandExecuteResult.fail(command, e)));
	}

	private CommandInterceptorContext<T> createInterceptorContext(int position) {
		return new CommandInterceptorContext<T>() {

			@Override
			public Publisher<CommandExecuteResult<T>> next(T c) {
				CommandInterceptorContext<T> context = createInterceptorContext(position + 1);
				if (position == interceptors.size()) {;
					return finalInterceptor.intercept(c, context);
				}
				return interceptors.get(position).intercept(c, context);
			}

			@Override
			public CommandStack<T> getCommandUndoStack() {
				return undoCommandStack;
			}
			
			@Override
			public CommandStack<T> getCommandRedoStack() {
				return redoCommandStack;
			}
			
		};
	}
	
	private Publisher<Boolean> executeCommandAndSwapStack(CommandStack<T> sourceStack, CommandStack<T> targetStack) {
		if (sourceStack.isEmpty()) {
			return Mono.just(false);
		}
		
		T command = sourceStack.pop();
		return executeCommand(command)
			.doOnNext(result -> result.getUndoCommand().ifPresent(newCommand -> targetStack.push(newCommand)))
			.then(Mono.just(true));
	}

}
