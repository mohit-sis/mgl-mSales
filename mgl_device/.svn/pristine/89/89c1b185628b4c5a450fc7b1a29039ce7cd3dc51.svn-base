/**
 * 
 */
package com.mobicule.msales.mgl.client.login.implementation;

import java.util.Hashtable;

import com.mobicule.msales.mgl.client.common.InputConstraints;
import com.mobicule.msales.mgl.client.common.Response;
import com.mobicule.msales.mgl.client.common.ValidationEngine;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginValidationService;

/**
 * @author nikita
 *
 */
public class DefaultLoginValidationService implements ILoginValidationService
{
	private Hashtable loginInputConstraints;

	private ValidationEngine validationEngine;

	private static ILoginValidationService instance = null;

	public static synchronized ILoginValidationService getInstance()
	{
		if (instance == null)
		{
			instance = new DefaultLoginValidationService();
		}
		return instance;
	}

	private DefaultLoginValidationService()
	{
		validationEngine = new ValidationEngine();

		loginInputConstraints = new Hashtable();

		loginInputConstraints.put("Username", new Object[] { new Object[] { InputConstraints.NON_EMPTY } });
		loginInputConstraints.put("Password", new Object[] { new Object[] { InputConstraints.NON_EMPTY } });
		loginInputConstraints.put("IMEINumber", new Object[] { new Object[] { InputConstraints.NON_EMPTY } });
	}

	public Response validateInput(String username, String password, String imeinumber)
	{

		String[] fieldNames = new String[] { "Username", "Password", "IMEINumber" };
		String[] fieldValues = new String[] { username, password, imeinumber };

		return validationEngine.validate(fieldNames, fieldValues, loginInputConstraints);
	}
}
