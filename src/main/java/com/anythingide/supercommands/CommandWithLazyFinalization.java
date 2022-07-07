package com.anythingide.supercommands;

import org.reactivestreams.Publisher;

/**
 * This interface represents a command whose result is destructive, but whose
 * destructive side effects should be delayed until later in case this command
 * is undone.
 * @param <T> Self class
 */
public interface CommandWithLazyFinalization<T extends CommandWithLazyFinalization<T>> extends Command<T> {

	/**
	 * Upon being subscribed to, execute this command's destructive side effects.
	 * @return The publisher that must be subscribed to.
	 */
	Publisher<Void> finish();
	
}
