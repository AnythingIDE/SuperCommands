package com.anythingide.supercommands;

import java.util.List;

import com.anythingide.supercommands.imp.CommandHistoryConfigBuilderImp;

/**
 * Represents the configuration used to create a CommandHistory instance.
 * @param <T> The command class.
 */
public interface CommandHistoryConfig<T extends Command<T>> {

	/**
	 * Get a list of the interceptors that will be applied
	 * to executed commands.
	 * @return The list of interceptors.
	 */
	List<CommandExecuteInterceptor<T>> getInterceptors();
	
	/**
	 * Get the strategy for undoing multiple commands in a row.
	 * @return The strategy.
	 */
	CommandMultiExecuteStrategy<T> getMultiUndoStrategy();
	
	/**
	 * Get the strategy for redoing multiple commands in a row.
	 * @return The strategy.
	 */
	CommandMultiExecuteStrategy<T> getMultiRedoStrategy();
	
	/**
	 * Create a builder for this interface.
	 * @param <T> The command class.
	 * @return The builder for this interface.
	 */
	public static <T extends Command<T>> CommandHistoryConfigBuilder<T> createBuilder() {
		return new CommandHistoryConfigBuilderImp<>();
	}
	
}
