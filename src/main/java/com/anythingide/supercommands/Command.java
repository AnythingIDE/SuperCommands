package com.anythingide.supercommands;

import org.reactivestreams.Publisher;

/**
 * This interface represents a command that can be executed.
 * @param <T> Own class
 */
public interface Command<T extends Command<T>> {
	
	/**
	 * Returns a publisher that, when subscribed to, executes this command.
	 * @return A publisher that, when subscribed to, executes this command
	 *  and optionally returns a command that undoes this command.
	 */
	Publisher<T> execute();
	
}
