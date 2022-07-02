package com.anythingide.supercommands;

import java.util.Optional;

import com.anythingide.supercommands.imp.CommandExecuteResultImp;

/**
 * Represents the result of executing a command.
 * Contains info about whether the command was successful,
 * and how it can be undone.
 */
public interface CommandExecuteResult<T extends Command<T>> {
	
	T getExecutedCommand();

	/**
	 * Get the command that reverses the result of this command, if one exists.
	 * Returns an empty optional if this command cannot be reversed.
	 * Should never return a command if this command failed.
	 * If the executed command is considered a redo, then any undo command it
	 * provides must do the exact opposite of the executed command.
	 * If the executed command is considered an undo, it should provide the same
	 * instance of the original redo command that created the executed command.
	 * @return An optional containing the command that reverses the result of
	 * 	this command, if one exists.
	 */
	Optional<T> getUndoCommand();
	
	/**
	 * Check whether this command completed as intended.
	 * @return A boolean indicating whether this command completed as intended.
	 */
	boolean wasSuccessful();
	
	/**
	 * Get the reason that the last executed command in this command chain failed,
	 * if it failed.
	 * @return An exception indicating the reason that the last executed command in
	 *  this command chain failed
	 */
	Optional<Throwable> getReasonForFailure();
	
	/**
	 * Create an ExecuteResult representing a successful command that can be undone.
	 * See the getUndoCommand method documentation for constraints on the undo command.
	 * @param undoCommand A command that undoes the command that was just performed.
	 * @return The created ExecuteResult.
	 */
	public static <T extends Command<T>> CommandExecuteResult<T> undoableSuccess(T command, T undoCommand) {
		return new CommandExecuteResultImp<T>(command, Optional.of(undoCommand), Optional.empty());
	};
	
	/**
	 * Create an ExecuteResult representing a successful command that can't
	 * be undone.
	 * @return The created ExecuteResult.
	 */
	public static <T extends Command<T>> CommandExecuteResult<T> destructiveSuccess(T command) {
		return new CommandExecuteResultImp<T>(command, Optional.empty(), Optional.empty());
	}
	
	/**
	 * Create an ExecuteResult representing a successful command that failed.
	 * @return The created ExecuteResult.
	 */
	public static <T extends Command<T>> CommandExecuteResult<T> fail(T command, Throwable reason) {
		return new CommandExecuteResultImp<T>(command, Optional.empty(), Optional.of(reason));
	}
	
	//TODO: Add params to JavaDocs
	
}
