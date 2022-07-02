package com.anythingide.supercommands;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Mono;

public class CommandHistoryTest {

	@Test
	@DisplayName("Command can be executed")
	@SuppressWarnings("unchecked")
	public void commandCanBeExecuted() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.empty());
		block(history.submit(command));
		Mockito.verify(command).execute();
	}
	
	@Test
	@DisplayName("Command can be undone")
	@SuppressWarnings("unchecked")
	public void commandCanBeUndone() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand undoCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand));
		Mockito.when(undoCommand.execute()).thenReturn(Mono.just(command));
		block(history.submit(command));
		Assertions.assertTrue(block(history.undo()));
		Mockito.verify(undoCommand).execute();
	}
	
	@Test
	@DisplayName("Command can be redone")
	@SuppressWarnings("unchecked")
	public void commandCanBeRedone() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand undoCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand));
		Mockito.when(undoCommand.execute()).thenReturn(Mono.just(command));
		block(history.submit(command));
		Assertions.assertTrue(block(history.undo()));
		Assertions.assertTrue(block(history.redo()));
		Mockito.verify(command, Mockito.times(2)).execute();
	}
	
	@Test
	@DisplayName("Redone command can be undone")
	@SuppressWarnings("unchecked")
	public void redoneCommandCanBeUndone() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand undoCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand));
		Mockito.when(undoCommand.execute()).thenReturn(Mono.just(command));
		block(history.submit(command));
		Assertions.assertTrue(block(history.undo()));
		Assertions.assertTrue(block(history.redo()));
		Assertions.assertTrue(block(history.undo()));
		Mockito.verify(undoCommand, Mockito.times(2)).execute();
	}
	
	@Test
	@DisplayName("Command cannot be undone twice")
	@SuppressWarnings("unchecked")
	public void commandCannotBeUndoneTwice() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand undoCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand));
		Mockito.when(undoCommand.execute()).thenReturn(Mono.just(command));
		block(history.submit(command));
		Assertions.assertTrue(block(history.undo()));
		Assertions.assertFalse(block(history.undo()));
		Mockito.verify(undoCommand).execute();
	}
	
	@Test
	@DisplayName("Command cannot be redone twice")
	@SuppressWarnings("unchecked")
	public void commandCannotBeRedoneTwice() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand undoCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand));
		Mockito.when(undoCommand.execute()).thenReturn(Mono.just(command));
		block(history.submit(command));
		Assertions.assertTrue(block(history.undo()));
		Assertions.assertTrue(block(history.redo()));
		Assertions.assertFalse(block(history.redo()));
		Mockito.verify(undoCommand).execute();
	}
	
	@Test
	@DisplayName("Interceptors can transform commands")
	@SuppressWarnings("unchecked")
	public void interceptorsCanTransformCommands() {
		TestCommand command = Mockito.mock(TestCommand.class);
		TestCommand interceptedCommand = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.empty());
		Mockito.when(interceptedCommand.execute()).thenReturn(Mono.empty());
		CommandExecuteInterceptor<TestCommand> interceptor = Mockito.mock(CommandExecuteInterceptor.class);
		Mockito.when(interceptor.intercept(Mockito.any(), Mockito.any()))
			.then(invocation -> invocation.<CommandInterceptorContext<TestCommand>>getArgument(1).next(interceptedCommand));
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of(interceptor));
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		block(history.submit(command));
		Mockito.verify(command, Mockito.never()).execute();
		Mockito.verify(interceptedCommand).execute();
	}
	
	/*@Test
	@DisplayName("Multi-undo undoes multiple commands")
	@SuppressWarnings("unchecked")
	public void multiUndoUndoesMultipleCommands() {
		CommandHistoryConfig<TestCommand> configs = Mockito.mock(CommandHistoryConfig.class);
		Mockito.when(configs.getInterceptors()).thenReturn(List.of());
		CommandHistory<TestCommand> history = CommandHistory.create(configs);
		TestCommand undoCommand1 = Mockito.mock(TestCommand.class);
		TestCommand undoCommand2 = Mockito.mock(TestCommand.class);
		TestCommand command = Mockito.mock(TestCommand.class);
		Mockito.when(command.execute()).thenReturn(Mono.just(undoCommand1), Mono.just(undoCommand2));
		Mockito.when(undoCommand1.execute()).thenReturn(Mono.just(command));
		Mockito.when(undoCommand2.execute()).thenReturn(Mono.just(command));
		InOrder undoOrder = Mockito.inOrder(undoCommand1, undoCommand2);
		block(history.submit(command));
		block(history.submit(command));
		Assertions.assertEquals(block(history.undo()), 2);
		undoOrder.verify(undoCommand2).execute();
		undoOrder.verify(undoCommand1).execute();
	}*/
	
	private static <T> T block(Publisher<T> publisher) {
		return Mono.from(publisher).block();
	}
	
}
