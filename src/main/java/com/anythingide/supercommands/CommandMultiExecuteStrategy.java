package com.anythingide.supercommands;

import java.util.function.Supplier;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.imp.CommandMultiExecuteStrategyImp;

/**
 * Represents the strategy used to execute multiple commands.
 * @param <T> The command class.
 */
public interface CommandMultiExecuteStrategy<T extends Command<T>> {

	/**
	 * When subscribed to, executes multiple commands in a row. If
	 * execution fails, a CommandChainFailedException will be emitted
	 * through the publisher that is returned.
	 * @param executor A supplier that, when invoked, will return a publisher
	 *  for the next command to execute.
	 * @param numberOfCommands The number of commands to be executed.
	 * @return The number of commands that were actually executed.
	 */
	Publisher<Integer> executeCommands(Supplier<Publisher<Boolean>> executor, int numberOfCommands);
	
	/**
	 * Create an instance of the default strategy.
	 * @param <T> The command class.
	 * @return The created instance.
	 */
	public static <T extends Command<T>> CommandMultiExecuteStrategy<T> createDefault() {
		return new CommandMultiExecuteStrategyImp<>();
	}
	
}
