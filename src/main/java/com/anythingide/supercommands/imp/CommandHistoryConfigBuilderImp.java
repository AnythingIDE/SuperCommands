package com.anythingide.supercommands.imp;

import java.util.ArrayList;
import java.util.List;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandExecuteInterceptor;
import com.anythingide.supercommands.CommandHistoryConfig;
import com.anythingide.supercommands.CommandHistoryConfigBuilder;
import com.anythingide.supercommands.CommandMultiExecuteStrategy;

public class CommandHistoryConfigBuilderImp<T extends Command<T>> implements CommandHistoryConfigBuilder<T> {

	private final List<CommandExecuteInterceptor<T>> interceptors = new ArrayList<>();
	
	private CommandMultiExecuteStrategy<T> multiUndoStrategy = new CommandMultiExecuteStrategyImp<>();
	private CommandMultiExecuteStrategy<T> multiRedoStrategy = new CommandMultiExecuteStrategyImp<>();
	
	@Override
	public void addIntereceptor(CommandExecuteInterceptor<T> interceptor) {
		interceptors.add(interceptor);
	}
	
	@Override
	public void setMultiUndoStrategy(CommandMultiExecuteStrategy<T> multiUndoStrategy) {
		this.multiUndoStrategy = multiUndoStrategy;
	}
	
	@Override
	public void setMultiRedoStrategy(CommandMultiExecuteStrategy<T> multiRedoStrategy) {
		this.multiRedoStrategy = multiUndoStrategy;
	}

	@Override
	public CommandHistoryConfig<T> build() {
		return new CommandHistoryConfig<T>() {

			@Override
			public List<CommandExecuteInterceptor<T>> getInterceptors() {
				return List.copyOf(interceptors);
			}

			@Override
			public CommandMultiExecuteStrategy<T> getMultiUndoStrategy() {
				return multiUndoStrategy;
			}

			@Override
			public CommandMultiExecuteStrategy<T> getMultiRedoStrategy() {
				return multiRedoStrategy;
			}
			
		};
	}

}
