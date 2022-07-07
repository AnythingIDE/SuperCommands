package com.anythingide.supercommands.interceptors;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandExecuteResult;
import com.anythingide.supercommands.CommandInterceptorContext;
import com.anythingide.supercommands.CommandWithLazyFinalization;

import reactor.core.publisher.Mono;

/**
 * This interceptor allows for running destructive commands
 * that would like to delay their destructive results and
 * implement CommandWithLazyFinalization.
 * @param <T> The command class.
 */
public class CommandDelayedFinalizationInterceptor<T extends Command<T>> implements CommandExecuteInterceptor<T> {

	private final List<Command<T>> commandsToFinalize = new ArrayList<>();
	
	@Override
	public Publisher<CommandExecuteResult<T>> intercept(T command, CommandInterceptorContext<T> context) {
		return Mono.from(context.next(command))
			.doOnNext(result -> {
				if (!result.wasSuccessful() || !(command instanceof CommandWithLazyFinalization)) {
					return;
				}
				
				commandsToFinalize.add(command);
			});
	}
	
	/**
	 * Finish the process of running destructive commands.
	 */
	public void finish() {
		for (Command<T> command: commandsToFinalize) {
			((CommandWithLazyFinalization<?>) command).finish();
		}
		commandsToFinalize.clear();
	}

}
