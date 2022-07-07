package com.anythingide.supercommands;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.imp.CommandHistoryImp;

/**
 * This interface is used to keep track of commands that can be undone
 * and redone.
 * @param <T> The command class.
 */
public interface CommandHistory<T extends Command<T>> {

	/**
	 * Upon being subscribed to, undo the last n commands that can be undone,
	 * if they can be undone. If the commands fail, a CommandChainFailedException
	 * will be emitted through the publisher.
	 * @param commands Represents n, the number of commands to be undone.
	 * @return The publisher that must be subscribed to.
	 *  It publishes a number indicating how many commands were undone.
	 */
	Publisher<Integer> undo(int commands);
	
	/**
	 * Upon being subscribed to, redo the last n commands that can be redone,
	 * if they can be redone. If the commands fail, a CommandChainFailedException
	 * will be emitted through the publisher.
	 * @param commands Represents n, the number of commands to be redone.
	 * @return The publisher that must be subscribed to.
	 *  It publishes a number indicating how many commands were redone.
	 */
	Publisher<Integer> redo(int commands);

	/**
	 * Upon being subscribed to, undo the last command that can be undone,
	 * if it can be undone. If the command fails, a CommandFailedException
	 * will be emitted through the publisher.
	 * @return The publisher that must be subscribed to.
	 *  It publishes a boolean indicating if there were any commands
	 *  to undo.
	 */
	Publisher<Boolean> undo();
	
	/**
	 * Upon being subscribed to, redo the last command that can be redone,
	 * if it can be redone. If the command fails, a CommandFailedException
	 * will be emitted through the publisher.
	 * @return The publisher that must be subscribed to.
	 *  It publishes a boolean indicating if there were any commands
	 *  to redo.
	 */
	Publisher<Boolean> redo();
	
	/**
	 * Upon being subscribed to, execute the specified command.
	 * If the command fails, a CommandFailedException will be emitted
	 * through the publisher.
	 * @param command The command to be executed.
	 * @return The publisher that must be subscribed to.
	 */
	Publisher<Void> submit(T command);
	
	/**
	 * Create an instance of the CommandHistory interface with the
	 * given configs.
	 * @param <T> The command class.
	 * @param configs The configs to create the instance with.
	 * @return The created instance.
	 */
	public static <T extends Command<T>> CommandHistory<T> create(CommandHistoryConfig<T> configs) {
		return new CommandHistoryImp<T>(configs);
	}
	
}
