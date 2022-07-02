package com.anythingide.supercommands.imp;

import java.util.Optional;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteResult;

public class CommandExecuteResultImp<T extends Command<T>> implements CommandExecuteResult<T> {

	private final T executedCommand;
	private final Optional<T> undoCommand;
	private final Optional<Throwable> failReason;

	public CommandExecuteResultImp(T executedCommand, Optional<T> undoCommand, Optional<Throwable> failReason) {
		this.executedCommand = executedCommand;
		this.undoCommand = undoCommand;
		this.failReason = failReason;
	}
	
	@Override
	public T getExecutedCommand() {
		return this.executedCommand;
	}

	@Override
	public Optional<T> getUndoCommand() {
		return this.undoCommand;
	}

	@Override
	public boolean wasSuccessful() {
		return this.failReason.isEmpty();
	}

	@Override
	public Optional<Throwable> getReasonForFailure() {
		return failReason;
	}

}
