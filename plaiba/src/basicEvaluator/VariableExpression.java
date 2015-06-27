package basicEvaluator;

public class VariableExpression extends Expression {
	private int nameIndex;
	
	public VariableExpression(int nameIndex)
	{
		this.nameIndex = nameIndex;
	}
	
	public int getVariable()
	{
		return nameIndex;
	}

	@Override
	public Integer eval(Evaluator evaluator, RuntimeEnvironment localEnv)
		throws EvaluatorException
	{
		Integer value;
		RuntimeEnvironment globalEnv = evaluator.getGlobalEnv();
		SymbolTable symbolTable = evaluator.getSymbolTable();
		
		if (localEnv.isBound(nameIndex))
		{
			value = localEnv.fetch(nameIndex);
		}
		else if (globalEnv.isBound(nameIndex))
		{
			value = globalEnv.fetch(nameIndex);
		}
		else
		{
			throw new EvaluatorException("Undefined variable: " + 
					symbolTable.getName(nameIndex));
		}
		
		return value;
	}
}
