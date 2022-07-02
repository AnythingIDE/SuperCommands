package com.anythingide.supercommands;

import org.reactivestreams.Publisher;

public interface CommandInterceptorContext<T extends Command<T>> {

	// Note: guarantee any errors will result in a CommandFailedException
	/**
	 * Execute the next interceptor in the interceptor chain.
	 * @param command The command that the next interceptor
	 *  will attempt to execute.
	 * @return The result of executing the next interceptor.
	 */
	Publisher<CommandExecuteResult<T>> next(T command);
	
	/**
	 * Get the stack of commands the can be undone.
	 * @return The stack of commands that can be undone.
	 */
	CommandStack<T> getCommandUndoStack();

	/**
	 * Get the stack of commands the can be redone.
	 * @return The stack of commands that can be redone.
	 */
	CommandStack<T> getCommandRedoStack();
	
}
