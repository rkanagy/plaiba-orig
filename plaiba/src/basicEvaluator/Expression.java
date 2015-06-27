package basicEvaluator;

public abstract class Expression {
	public abstract Integer eval(Evaluator evaluator, RuntimeEnvironment localEnv) 
			throws EvaluatorException;
}
