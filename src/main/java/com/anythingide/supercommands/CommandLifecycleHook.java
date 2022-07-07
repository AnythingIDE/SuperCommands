package com.anythingide.supercommands;

/**
 * A hook that can be used to watch the lifecycle of commands.
 * @param <T> The command class.
 */
public interface CommandLifecycleHook<T extends Command<T>> {

	/**
	 * Executed before a command executes.
	 * @param command The command that will execute.
	 */
	void beforeCommandExecute(T command);
	
	/**
	 * Executed after a command executes.
	 * @param executeResult The result of the executed command.
	 */
	void afterCommandExecute(CommandExecuteResult<T> executeResult);
	
}
