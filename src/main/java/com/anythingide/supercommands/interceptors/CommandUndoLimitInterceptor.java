package com.anythingide.supercommands.interceptors;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandExecuteResult;
import com.anythingide.supercommands.CommandInterceptorContext;
import com.anythingide.supercommands.CommandStack;

/**
 * An interceptor that limits the number of commands
 * that can be on a command stack at once.
 * @param <T> The command class.
 */
public class CommandUndoLimitInterceptor<T extends Command<T>> implements CommandExecuteInterceptor<T> {

	private final int maxSize;
	
	/**
	 * Create an instance of the CommandUndoLimitInterceptor.
	 * @param maxSize The maximum number of commands that can
	 *  be held by the command stack.
	 */
	public CommandUndoLimitInterceptor(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public Publisher<CommandExecuteResult<T>> intercept(T command, CommandInterceptorContext<T> context) {
		CommandStack<T> stack = context.getCommandUndoStack();
		while (stack.size() >= maxSize) {
			stack.remove(maxSize - 1);
		}
		
		return context.next(command);
	}
	
	
}
