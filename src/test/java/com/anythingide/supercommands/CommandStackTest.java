package com.anythingide.supercommands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CommandStackTest {
	
	@Test
	@DisplayName("Command stack starts empty")
	public void commandStackStartsEmpty() {
		CommandStack<TestCommand> stack = CommandStack.createDefault();
		Assertions.assertEquals(stack.isEmpty(), true);
	}
	
	@Test
	@DisplayName("Command can be pushed on stack")
	public void commandCanBePushedOnStack() {
		CommandStack<TestCommand> stack = CommandStack.createDefault();
		TestCommand command = Mockito.mock(TestCommand.class);
		stack.push(command);
		Assertions.assertEquals(stack.size(), 1);
		Assertions.assertEquals(stack.get(0), command);
	}
	
	@Test
	@DisplayName("Two commands can be pushed on stack")
	public void twoCommandsCanBePushedOnStack() {
		CommandStack<TestCommand> stack = CommandStack.createDefault();
		TestCommand command1 = Mockito.mock(TestCommand.class);
		stack.push(command1);
		TestCommand command2 = Mockito.mock(TestCommand.class);
		stack.push(command2);
		Assertions.assertEquals(stack.size(), 2);
		Assertions.assertEquals(stack.get(0), command2);
		Assertions.assertEquals(stack.get(1), command1);
	}
	
	@Test
	@DisplayName("Command pushed on stack can be popped off")
	public void commandPushedOnStackCanBePoppedOff() {
		CommandStack<TestCommand> stack = CommandStack.createDefault();
		TestCommand command = Mockito.mock(TestCommand.class);
		stack.push(command);
		Assertions.assertEquals(stack.pop(), command);
	}
	
}
