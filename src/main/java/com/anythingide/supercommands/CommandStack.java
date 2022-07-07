package com.anythingide.supercommands;

import java.util.List;

import com.anythingide.supercommands.imp.CommandStackImp;

/**
 * A stack that commands can be placed on.
 * @param <T> The command class.
 */
public interface CommandStack<T extends Command<T>> extends List<T> {

	/**
	 * Get a command from this stack and remove it.
	 * @return The command that was popped off of the stack.
	 */
	T pop();
	
	/**
	 * Put a command onto the top of this stack.
	 * @param command The command to push onto this stack.
	 */
	void push(T command);
	
	/**
	 * Create an instance of this interface.
	 * @param <T> The command class.
	 * @return The created interface.
	 */
	public static <T extends Command<T>> CommandStack<T> createDefault() {
		return new CommandStackImp<T>();
	}
	
}
