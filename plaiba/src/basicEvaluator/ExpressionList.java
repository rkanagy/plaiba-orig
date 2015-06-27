package basicEvaluator;

public class ExpressionList {
	private Expression expression;
	private ExpressionList expressionList;
	
	public ExpressionList(Expression expr, ExpressionList exprList)
	{
		expression = expr;
		expressionList = exprList;
	}
	
	public Expression getExpression()
	{
		return expression;
	}
	
	public ExpressionList getExpressionList()
	{
		return expressionList;
	}
}
