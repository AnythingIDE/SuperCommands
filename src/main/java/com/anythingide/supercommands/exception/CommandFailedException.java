package com.anythingide.supercommands.exception;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteResult;

public class CommandFailedException extends Exception {

	private static final long serialVersionUID = -7062466587579036132L;
	
	private final CommandExecuteResult<?> executeResult;
	
	public CommandFailedException(CommandExecuteResult<?> executeResult) {
		this.executeResult = executeResult;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Command<T>> CommandExecuteResult<T> getExecuteResult() {
		return (CommandExecuteResult<T>) executeResult;
	}

}
