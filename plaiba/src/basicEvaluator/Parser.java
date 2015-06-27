package basicEvaluator;

import java.io.IOException;
import java.util.LinkedList;

public class Parser {
	private final int maxNames = 100;
	private Scanner scanner;
	private SymbolTable symbolTable;
	private Evaluator evaluator;
	
	public Parser()
	{
		scanner = new Scanner();
		symbolTable = new SymbolTable(maxNames);
		evaluator = new Evaluator(symbolTable);
	}
	
	public void repl() throws IOException
	{
		boolean quittingTime = false;
        Expression currentExpression;
        
        // read-evaluation-print loop
        while (!(quittingTime))
        {
        	try
        	{
            	// get input from user
                scanner.Reader();
                if (scanner.Matches(scanner.getPosition(), 4, "quit".toCharArray()))
                {
                	quittingTime = true;
                	System.out.println("You have quit the interpreter");
                }
                else if (scanner.getUserInput(scanner.getPosition()) == '(' && 
                		scanner.Matches(
                				scanner.SkipBlanks(scanner.getPosition() + 1), 6 , "define".toCharArray()))
                {
                	symbolTable.printName(parseDef());
                	System.out.println();
                }
                else
                {
                	currentExpression = parseExpression();
                	RuntimeEnvironment localEnv = new RuntimeEnvironment();
                	System.out.print(evaluator.eval(currentExpression, localEnv));
                	System.out.println();
                	System.out.println();
                }
        	}
        	catch(EvaluatorException ex)
        	{
        		System.out.println(ex.getMessage());
        	}
        }
	}
	
	private int parseDef() throws EvaluatorException
	{
		int fname;
		LinkedList<Integer> nameList;
		Expression expr;
		
		scanner.SkipBlanksRelative(1); // skip "("
		scanner.SkipBlanksRelative(6); // skip "define"
		fname = parseName();
		scanner.SkipBlanksRelative(1); // skip "("
		nameList = parseNameList();
		expr = parseExpression();
		scanner.SkipBlanksRelative(1); // skip ")..."
		symbolTable.addFunctionDefinition(fname, nameList, expr);
		
		return fname;
	}
	
	private int parseName() throws EvaluatorException
	{
		String name = scanner.scanName();		
		return symbolTable.addName(name);
	}
	
	private LinkedList<Integer> parseNameList() throws EvaluatorException
	{
		int nameIndex;
		LinkedList<Integer> nameList = new LinkedList<Integer>();
		boolean done = false;
		
		while (!done)
		{
			if (scanner.getCurrentChar() == ')')
			{
				scanner.SkipBlanksRelative(1); // skip ")..."
				done = true;
			}
			else
			{
				nameIndex = this.parseName();
				nameList.add(nameIndex);
			}
		}
		
		return nameList;
	}
	
	private int parseVal()
	{
		return scanner.scanNumber();
	}
	
	private Expression parseExpression() throws EvaluatorException
	{
		Expression expr = null;
		int nameIndex;
		ExpressionList exprList;
		
		if (scanner.getCurrentChar() == '(')
		{
			scanner.SkipBlanksRelative(1); // skip "("
			nameIndex = parseName();
			exprList = parseExpressionList();
			expr = new ApplicationExpression(nameIndex, exprList);
		}
		else if (scanner.isNumber())
		{
			expr = new ValueExpression(parseVal());
		}
		else
		{
			expr = new VariableExpression(parseName());			
		}
		return expr;
	}
	
	private ExpressionList parseExpressionList() throws EvaluatorException
	{
		Expression expr;
		ExpressionList exprList;
		
		if (scanner.getCurrentChar() == ')')
		{
			scanner.SkipBlanksRelative(1);
			return null;
		}
		else
		{
			expr = parseExpression();
			exprList = parseExpressionList();
			return new ExpressionList(expr, exprList);
		}
	}
}
