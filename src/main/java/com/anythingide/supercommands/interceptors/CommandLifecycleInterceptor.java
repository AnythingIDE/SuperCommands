package com.anythingide.supercommands.interceptors;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandExecuteResult;
import com.anythingide.supercommands.CommandInterceptorContext;
import com.anythingide.supercommands.CommandLifecycleHook;
import com.anythingide.supercommands.exception.CommandFailedException;

import reactor.core.publisher.Mono;

/**
 * An interceptor that allows the life-cycle of commands
 * to be hooked into.
 * @param <T> The command class.
 */
public class CommandLifecycleInterceptor<T extends Command<T>> implements CommandExecuteInterceptor<T> {

	private CommandLifecycleHook<T> lifecycleHook;

	/**
	 * Create an instance of the CommandLifecycleInterceptor.
	 * @param lifecycleHook The hook that will watch command life-cycles.
	 */
	public CommandLifecycleInterceptor(CommandLifecycleHook<T> lifecycleHook) {
		this.lifecycleHook = lifecycleHook;
	}
	
	@Override
	public Publisher<CommandExecuteResult<T>> intercept(T command, CommandInterceptorContext<T> context) {
		lifecycleHook.beforeCommandExecute(command);
		return Mono.from(context.next(command))
			.doOnError(e -> lifecycleHook.afterCommandExecute(((CommandFailedException) e).getExecuteResult()))
			.doOnNext(executeResult -> lifecycleHook.afterCommandExecute(executeResult));
	}

}
