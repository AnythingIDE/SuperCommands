package com.anythingide.supercommands.imp;

import java.util.LinkedList;

import com.anythingide.supercommands.Command;
import com.anythingide.supercommands.CommandStack;

public class CommandStackImp<T extends Command<T>> extends LinkedList<T> implements CommandStack<T> {

	private static final long serialVersionUID = -5333171626694558669L;
	
}
