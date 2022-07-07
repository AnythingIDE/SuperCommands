package com.anythingide.supercommands;

/**
 * A builder for the CommandHistoryConfig interface.
 * @param <T> The command class.
 */
public interface CommandHistoryConfigBuilder<T extends Command<T>> {
	
	/**
	 * Add a command interceptor to this configuration.
	 * @param interceptor The interceptor to be added.
	 */
	void addIntereceptor(CommandExecuteInterceptor<T> interceptor);

	/**
	 * Set the strategy that will be used for undoing multiple commands.
	 * @param multiUndoStrategy The strategy that will be used for undoing multiple commands
	 */
	void setMultiUndoStrategy(CommandMultiExecuteStrategy<T> multiUndoStrategy);

	/**
	 * Set the strategy that will be used for redoing multiple commands.
	 * @param multiRedoStrategy The strategy that will be used for redoing multiple commands
	 */
	void setMultiRedoStrategy(CommandMultiExecuteStrategy<T> multiRedoStrategy);
	
	/**
	 * Build an instance of the CommandHistoryConfig interface using
	 * the data supplied to this builder.
	 * @return The built instance.
	 */
	CommandHistoryConfig<T> build();
	
}
