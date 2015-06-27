package basicEvaluator;

public class ValueExpression extends Expression {
	private int value;
	
	public ValueExpression(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}

	@Override
	public Integer eval(Evaluator evaluator, RuntimeEnvironment localEnv)
	{
		return value;
	}
}
