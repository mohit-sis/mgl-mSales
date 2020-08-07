package com.mobicule.android.component.db;

public class Clause
{
	private QueryItem leftItem;

	private Operator operator;

	private Clause rightItem;

	public Clause()
	{
		// TODO Auto-generated constructor stub
	}

	public Clause(QueryItem leftItem, Operator operator, Clause rightItem)
	{
		super();
		this.leftItem = leftItem;
		this.operator = operator;
		this.rightItem = rightItem;
	}

	public QueryItem getLeftItem()
	{
		return leftItem;
	}

	public void setLeftItem(QueryItem leftItem)
	{
		this.leftItem = leftItem;
	}

	public Operator getOperator()
	{
		return operator;
	}

	public void setOperator(Operator operator)
	{
		this.operator = operator;
	}

	public Clause getRightItem()
	{
		return rightItem;
	}

	public void setRightItem(Clause rightItem)
	{
		this.rightItem = rightItem;
	}

	@Override
	public String toString()
	{
		return "Clause [leftItem=" + leftItem + ", operator=" + operator + ", rightItem=" + rightItem + "]";
	}
}
