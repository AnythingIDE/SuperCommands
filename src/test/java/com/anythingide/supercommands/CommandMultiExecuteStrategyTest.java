package com.anythingide.supercommands;

import java.util.Stack;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Mono;

public class CommandMultiExecuteStrategyTest {

	@DisplayName("Multi-execute returns zero when the command stack has no commands on it")
	@Test
	public void multiExecuteReturnsZeroOnNoCommandsExecutable() {
		CommandMultiExecuteStrategy<TestCommand> strategy = CommandMultiExecuteStrategy.createDefault();
		int commandsExecuted = block(strategy.executeCommands(() -> Mono.just(false), 1));
		Assertions.assertEquals(0, commandsExecuted);
	}
	
	@Test
	@DisplayName("Multi-undo undoes multiple commands")
	@SuppressWarnings("unchecked")
	public void multiUndoUndoesMultipleCommands() {
		CommandMultiExecuteStrategy<TestCommand> strategy = CommandMultiExecuteStrategy.createDefault();
		TestCommand undoCommand1 = Mockito.mock(TestCommand.class);
		TestCommand undoCommand2 = Mockito.mock(TestCommand.class);
		Mockito.when(undoCommand1.execute()).thenReturn(Mono.empty());
		Mockito.when(undoCommand2.execute()).thenReturn(Mono.empty());
		InOrder undoOrder = Mockito.inOrder(undoCommand1, undoCommand2);
		Stack<TestCommand> stack = Mockito.mock(Stack.class);
		Mockito.when(stack.pop()).thenReturn(undoCommand2, undoCommand1);
		int commandsUndone = block(strategy.executeCommands(
			() -> Mono.from(stack.pop().execute()).then(Mono.just(true)), 2));
		Assertions.assertEquals(2, commandsUndone);
		undoOrder.verify(undoCommand2).execute();
		undoOrder.verify(undoCommand1).execute();
	}
	
	@Test
	@DisplayName("Multi-undo doesn't undo too many commands")
	@SuppressWarnings("unchecked")
	public void multiUndoDoesNotUndoTooManyCommands() {
		CommandMultiExecuteStrategy<TestCommand> strategy = CommandMultiExecuteStrategy.createDefault();
		TestCommand undoCommand1 = Mockito.mock(TestCommand.class);
		TestCommand undoCommand2 = Mockito.mock(TestCommand.class);
		Mockito.when(undoCommand1.execute()).thenReturn(Mono.empty());
		Mockito.when(undoCommand2.execute()).thenReturn(Mono.empty());
		InOrder undoOrder = Mockito.inOrder(undoCommand1, undoCommand2);
		Stack<TestCommand> stack = Mockito.mock(Stack.class);
		Mockito.when(stack.pop()).thenReturn(undoCommand2, undoCommand1);
		int commandsUndone = block(strategy.executeCommands(
			() -> Mono.from(stack.pop().execute()).then(Mono.just(true)), 1));
		Assertions.assertEquals(1, commandsUndone);
		undoOrder.verify(undoCommand2).execute();
		undoOrder.verify(undoCommand1, Mockito.never()).execute();
	}
	
	@Test
	@DisplayName("Multi-undo does not overreport undone commands")
	@SuppressWarnings("unchecked")
	public void multiUndoDoesNotOverreport() {
		CommandMultiExecuteStrategy<TestCommand> strategy = CommandMultiExecuteStrategy.createDefault();
		Supplier<Publisher<Boolean>> executor = Mockito.mock(Supplier.class);
		Mockito.when(executor.get()).thenReturn(Mono.just(true), Mono.just(false));
		int commandsUndone = block(strategy.executeCommands(executor, 2));
		Assertions.assertEquals(1, commandsUndone);
	}
	
	private static <T> T block(Publisher<T> publisher) {
		return Mono.from(publisher).block();
	}
	
}
