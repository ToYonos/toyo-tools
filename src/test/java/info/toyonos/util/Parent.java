package info.toyonos.util;

public class Parent
{
	private String name;
	private Parent child;
	
	public Parent(String name, Parent child)
	{
		this.name = name;
		this.child = child;
	}

	public String getName()
	{
		return name;
	}

	public Parent getChild()
	{
		return child;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
