package basicEvaluator;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class RuntimeEnvironment {
	private Map<Integer, Integer> env = new HashMap<Integer, Integer>();
	
	public RuntimeEnvironment()
	{
		// nothing to do, will have an empty environment
	}
	
	public RuntimeEnvironment(LinkedList<Integer> variables, LinkedList<Integer> values)
	{
		buildEnv(variables, values);
	}
	
	public void bindVar(Integer name, Integer value)
	{
		env.put(name, value);
	}
	
	public Integer findVar(Integer name)
	{
		return env.get(name);
	}
	
	public void assign(Integer name, Integer value)
	{
		env.put(name, value);
	}
	
	public Integer fetch(Integer name)
	{
		return env.get(name);
	}
	
	public boolean isBound(Integer name)
	{
		return env.containsKey(name);
	}
	
	private void buildEnv(LinkedList<Integer> variables, LinkedList<Integer> values)
	{
		for(int i = 0; i < variables.size(); i++)
		{
			Integer variable = variables.get(i);
			Integer value = values.get(i);
			env.put(variable,  value);
		}
	}
}
