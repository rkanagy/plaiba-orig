package basicEvaluator;

import java.util.LinkedList;

public class FunctionDefinition {
	private int nameIndex;
	private LinkedList<Integer> formals;
	private Expression body;
	
	public FunctionDefinition(int nameIndex, LinkedList<Integer> formals, 
			Expression body)
	{
		this.nameIndex = nameIndex;
		this.formals = formals;
		this.body = body;
	}
	
	public int getNameIndex()
	{
		return nameIndex;
	}
	
	public LinkedList<Integer> getFormals()
	{
		return formals;
	}
	
	public Expression getBody()
	{
		return body;
	}
	
	public void update(LinkedList<Integer> formals, Expression body)
	{
		this.formals = formals;
		this.body = body;
	}
}
