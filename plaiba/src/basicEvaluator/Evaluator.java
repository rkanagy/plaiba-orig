package basicEvaluator;

public class Evaluator {
	private RuntimeEnvironment globalEnv;
	private SymbolTable symbolTable;
	
	public Evaluator(SymbolTable symbolTable)
	{
		this.symbolTable = symbolTable;
		globalEnv = new RuntimeEnvironment();
	}
	
	public Integer eval(Expression expr, RuntimeEnvironment localEnv) 
			throws EvaluatorException
	{
		return expr.eval(this, localEnv);
	}
	
	public RuntimeEnvironment getGlobalEnv()
	{
		return globalEnv;
	}
	
	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}
}
