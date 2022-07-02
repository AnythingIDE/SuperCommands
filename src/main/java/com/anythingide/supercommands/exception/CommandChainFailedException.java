package com.anythingide.supercommands.exception;

/**
 * This exception indicates that 1 or more commands were being
 * executed at once, and one of the commands was not successful.
 */
public class CommandChainFailedException extends Exception {

	private static final long serialVersionUID = -7062466587579036132L;
	
	private final Throwable reason;
	private final int successfulCommands;

	/**
	 * Create a CommandChainFailedException.
	 * @param reason The reason that the command chain failed.
	 * @param successfulCommands The number of commands that executed
	 *  successfully before the command chain failed.
	 */
	public CommandChainFailedException(Throwable reason, int successfulCommands) {
		this.reason = reason;
		this.successfulCommands = successfulCommands;
	}
	
	/**
	 * Get the reason that the most recently executed command of this
	 * command chain failed.
	 * @return An exception representing the reason that the most
	 *  recently executed command of this command chain failed.
	 */
	public Throwable getReason() {
		return this.reason;
	}
	
	/**
	 * Get the number of commands that executed successfully before
	 * the command chain failed.
	 * @return the number of commands that executed successfully before
	 *  the command chain failed.
	 */
	public int getSuccessfulCommands() {
		return this.successfulCommands;
	}
	
}
