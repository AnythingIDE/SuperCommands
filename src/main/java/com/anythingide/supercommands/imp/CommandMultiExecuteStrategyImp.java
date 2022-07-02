package com.anythingide.supercommands.imp;

import java.util.function.Supplier;

import org.reactivestreams.Publisher;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandMultiExecuteStrategy;
import com.anythingide.supercommands.exception.CommandChainFailedException;

import reactor.core.publisher.Mono;

public class CommandMultiExecuteStrategyImp<T extends Command<T>> implements CommandMultiExecuteStrategy<T> {

	@Override
	public Publisher<Integer> executeCommands(Supplier<Publisher<Boolean>> executor, int numberOfCommands) {
		Mono<Integer> commandChain = Mono.just(0);
		for (int i = 0; i < numberOfCommands; i++) {
			int j = i;
			commandChain = commandChain
				.flatMap(numExecuted -> {
					if (numExecuted == j) {
						return Mono.from(executor.get())
							.map(b -> b ? j + 1 : numExecuted)
							.onErrorMap(e -> new CommandChainFailedException(e, j));
					} else {
						return Mono.just(numExecuted);
					}
				});
				
		}
		
		return commandChain;
	}

}
