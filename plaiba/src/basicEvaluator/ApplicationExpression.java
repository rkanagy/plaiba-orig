package basicEvaluator;

import java.util.LinkedList;

public class ApplicationExpression extends Expression {
	private int nameIndex;
	private ExpressionList exprList;
	
	public ApplicationExpression(int nameIndex, ExpressionList exprList)
	{
		this.nameIndex = nameIndex;
		this.exprList = exprList; 
	}
	
	public int getName()
	{
		return nameIndex;
	}
	
	public ExpressionList getExpressionList()
	{
		return exprList;
	}

	@Override
	public Integer eval(Evaluator evaluator, RuntimeEnvironment localEnv)
		throws EvaluatorException
	{
		Integer value;
		SymbolTable symTab = evaluator.getSymbolTable();
		
		if (symTab.isUserFunction(nameIndex))
		{
			value = applyUserFunction(evaluator, evalList(evaluator, exprList, localEnv));
		}
		else
		{
			SymbolTable.BuiltinOp op = symTab.primOp(nameIndex);
			if (symTab.isControlOp(op))
			{
				value = applyControlOp(evaluator, op, exprList, localEnv);
			}
			else 
			{
				value = applyValueOp(evaluator, op, 
						evalList(evaluator, exprList, localEnv), localEnv);
			}				
		}
		
		return value;
	}
	
	private Integer applyUserFunction(Evaluator evaluator, LinkedList<Integer> actuals) 
			throws EvaluatorException 
	{
		FunctionDefinition function = evaluator.getSymbolTable().fetchFunction(nameIndex);
		if (function == null)
		{
			throw new EvaluatorException("Undefined function: " + 
					evaluator.getSymbolTable().getName(nameIndex));
		}
		
		if (function.getFormals().size() != actuals.size())
		{
			throw new EvaluatorException("Wrong number of arguments to " +
					evaluator.getSymbolTable().getName(nameIndex));
		}
		
		RuntimeEnvironment localEnv = new RuntimeEnvironment(function.getFormals(), 
				actuals);
		return evaluator.eval(function.getBody(), localEnv);
	}
	
	private Integer applyControlOp(Evaluator evaluator, SymbolTable.BuiltinOp op, 
			ExpressionList expressionList, RuntimeEnvironment localEnv)
			throws EvaluatorException
	{
		Integer value = 0;
		SymbolTable symTab = evaluator.getSymbolTable();
		
		switch(op)
		{
			case IFOP:
				if (symTab.isTrueVal(evaluator.eval(expressionList.getExpression(), 
						localEnv)))
				{
					value = evaluator.eval(
							expressionList.getExpressionList().getExpression(), 
							localEnv);
				}				
				else
				{
					value = evaluator.eval(
							expressionList.getExpressionList().getExpressionList().getExpression(), 
							localEnv);
				}					
				break;
				
			case WHILEOP:
				value = evaluator.eval(expressionList.getExpression(), localEnv);
				while (symTab.isTrueVal(value))
				{
					value = evaluator.eval(expressionList.getExpressionList().getExpression(),
							localEnv);
					value = evaluator.eval(expressionList.getExpression(), localEnv);
				}
				break;
				
			case SETOP:
				value = evaluator.eval(expressionList.getExpressionList().getExpression(), 
						localEnv);
				VariableExpression expr = (VariableExpression)expressionList.getExpression();
				int variable = expr.getVariable();
				RuntimeEnvironment globalEnv = evaluator.getGlobalEnv();
				
				if (localEnv.isBound(variable))
				{
					localEnv.assign(variable, value);
				}
				else
				{
					if (globalEnv.isBound(variable))
					{
						globalEnv.assign(variable, value);
					}
					else
					{
						globalEnv.bindVar(variable, value);
					}
				}
				break;
				
			case BEGINOP:
				ExpressionList beginExprList = expressionList;
				while (beginExprList.getExpressionList() != null)
				{
					value = evaluator.eval(beginExprList.getExpression(), localEnv);
					beginExprList = beginExprList.getExpressionList();
				}
				value = evaluator.eval(beginExprList.getExpression(), localEnv);
				break;
				
			default:
				break;
		}
		
		return value;
	}
	
	private Integer applyValueOp(Evaluator evaluator, SymbolTable.BuiltinOp op, 
			LinkedList<Integer> valueList, RuntimeEnvironment localEnv)
			throws EvaluatorException
	{
		Integer n = 0, n1 = 0, n2 = 0;
		SymbolTable symTab = evaluator.getSymbolTable();
	    int arity = arity(op, symTab);
	    
		if (arity != valueList.size())
		{
			System.out.print("Wrong number of arguments to ");
			symTab.printName(op.ordinal());
			System.out.println();
			throw new EvaluatorException("Wrong number of arguments to " + 
					symTab.getName(op.ordinal()));
		}
		
		n1 = valueList.getFirst();
		if (arity == 2) n2 = valueList.getLast();
		switch (op)
		{
			case PLUSOP:
				n = n1 + n2;
				break;
				
			case MINUSOP:
				n = n1 - n2;
				break;
				
			case TIMESOP:
				n = n1 * n2;
				break;
				
			case DIVOP:
				n = n1 / n2;
				break;
				
			case EQOP:
				if (n1 == n2) n = 1; else n = 0;
				break;
				
			case LTOP:
				if (n1 < n2) n = 1; else n = 0;
				break;
				
			case GTOP:
				if (n1 > n2) n = 1; else n = 0;
				break;
				
			case PRINTOP:
				System.out.println(n1);
				n = n1;
				break;
			
			default:
				break;
		}
		
		return n;
	}
	
	private int arity(SymbolTable.BuiltinOp op, SymbolTable symTab)
	{
		return (symTab.isArity2op(op) ? 2 : 1);
	}
	
	private LinkedList<Integer> evalList(Evaluator evaluator, 
			ExpressionList expressionList, RuntimeEnvironment localEnv)
			throws EvaluatorException
	{
		Integer value;
		LinkedList<Integer> valueList = new LinkedList<Integer>();
		
		ExpressionList exprList = expressionList;		
		while (exprList != null)
		{
			value = evaluator.eval(exprList.getExpression(), localEnv);
			valueList.add(value);
			exprList = exprList.getExpressionList();
		}
		
		return valueList;
	}
}
