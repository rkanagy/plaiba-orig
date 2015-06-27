package basicEvaluator;

import java.io.*;

public class Scanner {
    private final String prompt = "-> ";
    private final String prompt2 = "> ";
    private final int maxInput = 500;
    private final char endOfLineChar = 10;
    private final char tabChar = 9;
    private final char commentChar = ';';
    private final int nameLength = 20;
    
    private PushbackInputStream in;
    private int position;
    private int inputLength;
    private char[] userInput;
    private char currentChar;
    
	private String delimiters = "() ;"; // right/left parentheses, blank, and semicolon
    private String digits = "0123456789";
    
    public Scanner()
    {
    	in = new PushbackInputStream(System.in, 1);
        userInput = new char[maxInput];    	
    }
    
    public void Reader() throws IOException, EvaluatorException
    {
    	do
    	{
    		ReadInput();
    		position = SkipBlanks(0); // userInput array zero-based
    	} while (position >= inputLength); 
    }
    
    public int SkipBlanks(int pos)
    {
    	while (userInput[pos] == ' ')
    	{
    		pos++;
    	}
    	
    	return pos;
    }
    
    public void SkipBlanksRelative(int relPos)
    {
    	position += relPos;
    	while (userInput[position] == ' ')
    	{
    		position++;
    	}
    }
    
    public boolean Matches(int s, int leng, char[] nm)
    {
    	boolean match = true;
    	int i = 0;
    	
    	while (match && (i < leng))
    	{
    		if (userInput[s] != nm[i]) match = false;
    		i++;
    		s++;
    	}
    	
    	if (!(IsDelim(userInput[s]))) match = false;
    	return match;
    }
    
    public boolean IsDelim(char c)
    {
    	return (delimiters.indexOf(c) != -1);
    }
    
    public int getPosition()
    {
    	return position;
    }
    
    public char getUserInput(int pos)
    {
    	return userInput[pos];
    }
    
    public char getCurrentChar()
    {
    	return userInput[position];
    }
    
    public String scanName() throws EvaluatorException
    {
    	String name = "";
    	int leng = 0;
    	
    	while (position <= inputLength && !IsDelim(userInput[position]))
    	{
    		if (leng == nameLength)
    		{
    			throw new EvaluatorException("Name too long, begins: " + name);
    		}
    		leng++;
    		name = name + userInput[position];
    		position++;
    	}
    	
    	if (leng == 0)
    	{
    		throw new EvaluatorException("Error: expected name, instead read: " + userInput[position]);
    	}
    	position = SkipBlanks(position);
    	
    	return name;
    }
    
    public int scanNumber()
    {
    	int n = 0;
    	int sign = 1;
    	
    	if (userInput[position] == '-')
    	{
    		sign = -1;
    		position++;
    	}
    	
    	while (digits.indexOf(userInput[position]) != -1)
    	{
    		n = 10 * n + Character.getNumericValue(userInput[position]) - 
    				Character.getNumericValue('0');
    		position++;
    	}
    	position = SkipBlanks(position);
    	
    	return n * sign;
    }
    
    public boolean isNumber()
    {
    	return isDigits(position) || 
    			((userInput[position] == '-') && isDigits(position + 1));
    }
    
    private boolean isDigits(int pos)
    {
    	boolean result = false;
    	
    	if (digits.indexOf(userInput[pos]) != -1)
    	{
    		result = true;
    		while (digits.indexOf(userInput[pos]) != -1) { pos++; }
    		if (!IsDelim(userInput[pos])) result = false;
    	}
    	
    	return result;
    }
    
    private void ReadInput() throws IOException, EvaluatorException
    {
    	System.out.print(prompt);
    	position = -1; // arrays are zero-based, make sure first position is zero
    	do
    	{
    		position++;
    		if (position == maxInput)
    		{
    			throw new EvaluatorException("User input too long");
    		}
    		currentChar = NextChar();
    		userInput[position] = currentChar;
    		if (userInput[position] == '(')
    		{
    			ReadParens();
    		}
    	}
        while (!IsEndOfLine());
    	
    	inputLength = position + 1;    	
    	userInput[position + 1] = commentChar; // sentinel    	
    }
    
    private char NextChar() throws IOException
    {
    	char c = (char) in.read();
    	if (c == tabChar)
    	{
    		c = ' ';
    	}
    	else if (c == commentChar)
    	{
    		while (!IsEndOfLine())
    		{
    			c = (char) in.read();
    		}
    		c = ' ';
    	}
    	else if (c == endOfLineChar)
    	{
    		c = ' ';
    	}
    	
    	return c;
    }
    
    private void ReadParens() throws IOException, EvaluatorException
    {
    	int parenCount;
    	
    	parenCount = 1; // already read a left-parenthesis
    	do
    	{
    		if (IsEndOfLine()) System.out.print(prompt2);
    		currentChar = NextChar();
    		position++;
    		if (position == maxInput)
    		{
    			throw new EvaluatorException("User input too long");
    		}
    		userInput[position] = currentChar;
    		if (currentChar == '(') parenCount++;
    		if (currentChar == ')') parenCount--;
    	}
    	while (parenCount != 0);    		
    }
    
    private boolean IsEndOfLine() throws IOException
    {
    	boolean eoln = false;
    	char c;
    	
    	c = (char) in.read();
    	if (c == endOfLineChar) eoln = true;
    	in.unread(c);
    	
    	return eoln;
    }
}
