package com.anythingide.supercommands.imp;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandExecuteResult;
import com.anythingide.supercommands.CommandInterceptorContext;

import reactor.core.publisher.Mono;

public class CommandExecutorInterceptor<T extends Command<T>> implements CommandExecuteInterceptor<T> {

	@Override
	public Publisher<CommandExecuteResult<T>> intercept(T command, CommandInterceptorContext<T> context) {
		return Mono.from(command.execute())
			.map(result -> CommandExecuteResult.<T>undoableSuccess(command, result))
			.switchIfEmpty(Mono.just(CommandExecuteResult.<T>destructiveSuccess(command)));
	}

}
