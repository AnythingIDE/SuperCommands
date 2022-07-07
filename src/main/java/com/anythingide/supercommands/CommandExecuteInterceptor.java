package com.anythingide.supercommands;

import org.reactivestreams.Publisher;

/**
 * This interface is used to extend the behaviour executed
 * upon running commands.
 * @param <T> Command class
 */
public interface CommandExecuteInterceptor<T extends Command<T>> {

	/**
	 * This method gets called upon a command being intercepted.
	 * It should execute context.next with the intercepted command upon
	 * successfully intercepting and transforming the command.
	 * @param command The command that has been intercepted.
	 * @param context Represents info about this interception.
	 * @return The result of intercepting this command, and possibly
	 *  the command that got executed.
	 */
	Publisher<CommandExecuteResult<T>> intercept(T command, CommandInterceptorContext<T> context);
	
}
