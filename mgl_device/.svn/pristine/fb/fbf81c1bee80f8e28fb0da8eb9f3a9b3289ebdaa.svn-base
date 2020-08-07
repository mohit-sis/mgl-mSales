package com.mobicule.msales.mgl.client.common;

import java.util.Hashtable;

public class ValidationEngine
{
	public Response validate(String inputField, String value, Hashtable inputConstraints)
	{
		Object[] constraints = (Object[]) inputConstraints.get(inputField);

		for (int i = 0; i < constraints.length; i++)
		{
			Object[] currentConstraint = (Object[]) constraints[i];

			String constraintName = (String) currentConstraint[0];

			if (InputConstraints.NON_EMPTY.equals(constraintName))
			{
				if (value == null || value.trim().equals(""))
				{
					return new Response(false, inputField + " is Empty.", null);
				}
			}

			if (InputConstraints.LENGTH.equals(constraintName))
			{
				String constraintValue = (String) currentConstraint[1];

				if (value.length() != Integer.parseInt(constraintValue))
				{
					return new Response(false, inputField + " must be of " + constraintValue + " characters.", null);
				}
			}
		}

		return new Response(true, null, null);
	}

	public Response validate(String[] fieldNames, String[] fieldValues, Hashtable inputConstraints)
	{
		for (int i = 0; i < fieldNames.length; i++)
		{
			String fieldName = fieldNames[i];
			String fieldValue = fieldValues[i];

			Response response = validate(fieldName, fieldValue, inputConstraints);

			if (!response.isSuccess())
			{
				return response;
			}
		}
		return new Response(true, null, null);
	}
}
