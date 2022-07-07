package com.anythingide.supercommands.imp;

public class CommandException extends RuntimeException {

	private static final long serialVersionUID = -722172160654498656L;
	
	private final Exception exception;

	public CommandException(Exception exception) {
		super(exception);
		
		this.exception = exception;
	}
	
	public Exception getException() {
		return this.exception;
	}
	
}
