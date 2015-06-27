package basicEvaluator;

import java.util.LinkedList;

public class SymbolTable {
	private String[] names;
	private LinkedList<FunctionDefinition> functionDefinitions;
	private int numNames;
	private int numBuiltins;
	private int maxNames;
	
	public enum BuiltinOp { 
		IFOP(0), WHILEOP(1), SETOP(2), BEGINOP(3), PLUSOP(4), MINUSOP(5), 
		TIMESOP(6), DIVOP(7), EQOP(8), LTOP(9), GTOP(10), PRINTOP(11);
		
		private final static BuiltinOp[] vals = values();
		private final int value;
		private BuiltinOp(int value) { this.value = value; }
		
		public BuiltinOp next()
		{
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
	
	public SymbolTable(int maxNames)
	{
		this.maxNames = maxNames;
		names = new String[this.maxNames];
		initNames();
	}
	
	public int addName(String name) throws EvaluatorException
	{
		int i = 0;
		boolean found = false;
		
		while (i < numNames && !found)
		{
			if (names[i].compareTo(name) == 0) // strings are equal
			{
				found = true;
			}
			else
			{
				i++;
			}
		}
		
		if (!found)
		{
			if (i >= maxNames)
			{
				throw new EvaluatorException("No more room for names");
			}
			
			numNames = i + 1;
			names[i] = name;
		}
		
		return i;
	}
	
	public void addFunctionDefinition(int nameIndex, LinkedList<Integer> nameList, 
			Expression expr)
	{
		FunctionDefinition found = this.findFunctionDefinition(nameIndex);
		if (found == null)
		{
			found = new FunctionDefinition(nameIndex, nameList, expr);
			functionDefinitions.add(found);			
		}
		else
		{
			found.update(nameList, expr);
		}
	}
	
	public FunctionDefinition fetchFunction(int nameIndex)
	{
		FunctionDefinition foundFunction = null;
		
		for(FunctionDefinition function: functionDefinitions)
		{
			if (function.getNameIndex() == nameIndex)
			{
				foundFunction = function;
				break;
			}
		}
		
		return foundFunction;
	}
	
	public void printName(int nameIndex)
	{
		System.out.print(names[nameIndex]);
	}
	
	public String getName(int nameIndex)
	{
		return names[nameIndex];
	}
	
	public int getNumBuiltins()
	{
		return numBuiltins;
	}
	
	public boolean isValueOp(BuiltinOp op)
	{
		return (op.value >= BuiltinOp.PLUSOP.value && 
				op.value <= BuiltinOp.PRINTOP.value);
	}
	
	public boolean isControlOp(BuiltinOp op)
	{
		return (op.value >= BuiltinOp.IFOP.value &&
				op.value <= BuiltinOp.BEGINOP.value);
	}
	
	public boolean isUserFunction(int nameIndex)
	{
		return (nameIndex > (numBuiltins - 1));
	}
	
	public boolean isArity2op(BuiltinOp op)
	{
		return (op.value >= BuiltinOp.PLUSOP.value &&
				op.value <= BuiltinOp.GTOP.value);
	}
	public BuiltinOp primOp(int optr)
	{
		BuiltinOp op = BuiltinOp.IFOP;
		for (int i = 0; i < optr; i++) {
			op = op.next();
		}
		
		return op;
	}
	
	public boolean isTrueVal(Integer val)
	{
		return val != 0;
	}
	
	private void initNames()
	{
		int i = 0;
		
		functionDefinitions = new LinkedList<FunctionDefinition>();

		names[i] = "if"; i++;
		names[i] = "while"; i++;
		names[i] = "set"; i++;
		names[i] = "begin"; i++;
		names[i] = "+"; i++;
		names[i] = "-"; i++;
		names[i] = "*"; i++;
		names[i] = "/"; i++;
		names[i] = "="; i++;
		names[i] = "<"; i++;
		names[i] = ">"; i++;
		names[i] = "print"; i++;
		
		numNames = i;
		numBuiltins = i;
	}
	
	private FunctionDefinition findFunctionDefinition(int nameIndex)
	{
		FunctionDefinition func = null;
		
		for(FunctionDefinition f : functionDefinitions)
		{
			if (f.getNameIndex() == nameIndex)
			{
				func = f;
				break;
			}
		}
		
		return func;
	}
}
