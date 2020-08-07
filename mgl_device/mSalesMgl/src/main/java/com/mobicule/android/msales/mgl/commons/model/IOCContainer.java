package com.mobicule.android.msales.mgl.commons.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.mobicule.android.msales.mgl.jointicketing.model.DefaultJoinTicketingCommunicationService;
import com.mobicule.android.msales.mgl.jointicketing.model.DefaultJoinTicketingPersistenceService;
import com.mobicule.android.msales.mgl.login.model.DefaultLoginCommunicationService;
import com.mobicule.android.msales.mgl.login.model.DefaultLoginPersistenceService;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingCommunicationService;
import com.mobicule.android.msales.mgl.meterreading.model.DefaultMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.onmplaning.model.DefaultOnMPlanningCommunicationService;
import com.mobicule.android.msales.mgl.onmplaning.model.DefaultOnMPlanningPersistanceService;
import com.mobicule.android.msales.mgl.randommeterreading.model.DefaultRandomMeterReadingPersistenceService;
import com.mobicule.android.msales.mgl.updatecustomer.model.DefaultUpdateCustomerCommunicationService;
import com.mobicule.android.msales.mgl.updatecustomer.model.DefaultUpdateCustomerPersistanceService;
import com.mobicule.msales.mgl.client.application.ApplicationFacade;
import com.mobicule.msales.mgl.client.application.IApplicationFacade;
import com.mobicule.msales.mgl.client.application.IApplicationPersistence;
import com.mobicule.msales.mgl.client.jointicketing.DefaultJoinTicketingFacade;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingCommunication;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingFacade;
import com.mobicule.msales.mgl.client.jointicketing.IJoinTicketingPersistance;
import com.mobicule.msales.mgl.client.login.implementation.DefaultLoginFacade;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginCommunicationService;
import com.mobicule.msales.mgl.client.login.interfaces.ILoginPersistenceService;
import com.mobicule.msales.mgl.client.meterreading.DefaultMeterReadingFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingCommunication;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingFacade;
import com.mobicule.msales.mgl.client.meterreading.IMeterReadingPersistance;
import com.mobicule.msales.mgl.client.onmPlanning.DefaultOnMPlanningFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMCommunication;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMFacade;
import com.mobicule.msales.mgl.client.onmPlanning.IOnMPersistance;
import com.mobicule.msales.mgl.client.randommeterreading.DefaultRandomMeterReadingFacade;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReadingFacade;
import com.mobicule.msales.mgl.client.randommeterreading.IRandomMeterReadingPersistance;
import com.mobicule.msales.mgl.client.timevariation.DefaultTimeVariationFacade;
import com.mobicule.msales.mgl.client.timevariation.ITimeVariationFacade;
import com.mobicule.msales.mgl.client.updatecustomer.DefaultUpdateCustomerFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerCommunication;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerFacade;
import com.mobicule.msales.mgl.client.updatecustomer.IUpdateCustomerPersistance;
import com.mobicule.versioncontrol.VersionControl;

public class IOCContainer
{
	private static IOCContainer uniqueInstance;

	private Context context;

	private Map<String, Object> beanPool;

	public static final String VERSION_CONTROL = "VersionControl";

	private IOCContainer(Context context)
	{
		this.context = context;
		beanPool = new HashMap<String, Object>();
	}

	public static synchronized IOCContainer getInstance(Context context)
	{

		if (uniqueInstance == null)
		{
			uniqueInstance = new IOCContainer(context);
		}

		return uniqueInstance;
	}

	public Object getBean(String beanName)
	{
		Object bean = beanPool.get(beanName);

		if (bean == null)
		{
			bean = createBeanAndPutItInPool(beanName);
			beanPool.put(beanName, (Object) bean);
		}

		return bean;
	}

	private Object createBeanAndPutItInPool(String beanName)
	{
		if ("persistenceService".equals(beanName))
		{
			return DefaultLoginPersistenceService.getPersistenceService(context);
		}

		else if ("loginFacade".equals(beanName))
		{
			return new DefaultLoginFacade((ILoginCommunicationService) getBean("communicationService"),
					(ILoginPersistenceService) getBean("persistenceService"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("communicationService".equals(beanName))
		{
			return DefaultLoginCommunicationService.getCommunicationService(context);
		}
		else if ("ApplicationFacade".equals(beanName))
		{
			return ((IApplicationFacade) ApplicationFacade
					.getInstance((IApplicationPersistence) getBean("ApplicationPersistanceService")));
		}
		else if ("ApplicationPersistanceService".equals(beanName))
		{
			return (IApplicationPersistence) ApplicationPersistance.getApplicationPersistance(context);
		}
		else if ("DefaultMeterReadingFacade".equals(beanName))
		{
			return (IMeterReadingFacade) DefaultMeterReadingFacade.getInstance(
					(IMeterReadingPersistance) getBean("DefaultMeterReadingPersistanceService"),
					(IMeterReadingCommunication) getBean("DefaultMeterReadingCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("DefaultMeterReadingPersistanceService".equals(beanName))
		{
			return DefaultMeterReadingPersistenceService.getPersistenceService(context);
		}
		else if ("DefaultMeterReadingCommunicationService".equals(beanName))
		{
			return DefaultMeterReadingCommunicationService.getCommunicationService(context);
		}

		else if ("DefaultRandomMeterReadingFacade".equals(beanName))
		{
			return (IRandomMeterReadingFacade) DefaultRandomMeterReadingFacade.getInstance(
					(IRandomMeterReadingPersistance) getBean("DefaultRandomMeterReadingPersistanceService"),
					(IMeterReadingCommunication) getBean("DefaultMeterReadingCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("DefaultRandomMeterReadingPersistanceService".equals(beanName))
		{
			return DefaultRandomMeterReadingPersistenceService.getPersistenceService(context);
		}
		else if ("DefaultUpdateCustomerFacade".equals(beanName))
		{
			return (IUpdateCustomerFacade) DefaultUpdateCustomerFacade.getInstance(
					(IUpdateCustomerPersistance) getBean("DefaultCustomerUpdatePersistanceService"),
					(IUpdateCustomerCommunication) getBean("DefaultUpdateCustomerCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("DefaultCustomerUpdatePersistanceService".equals(beanName))
		{
			return DefaultUpdateCustomerPersistanceService.getPersistenceService(context);
		}
		else if ("DefaultUpdateCustomerCommunicationService".equals(beanName))
		{
			return DefaultUpdateCustomerCommunicationService.getCommunicationService(context);
		}
		else if ("DefaultOnMPlanningFacade".equals(beanName))
		{
			return (IOnMFacade) DefaultOnMPlanningFacade.getInstance(
					(IOnMPersistance) getBean("DefaultOnMPlanningPersistanceService"),
					(IOnMCommunication) getBean("DefaultOnMPlanningCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("DefaultOnMPlanningPersistanceService".equals(beanName))
		{
			return DefaultOnMPlanningPersistanceService.getPersistenceService(context);
		}
		else if ("DefaultOnMPlanningCommunicationService".equals(beanName))
		{
			return DefaultOnMPlanningCommunicationService.getCommunicationService(context);
		}
		else if ("DefaultTimeVarationFacade".equals(beanName))
		{
			return (ITimeVariationFacade) DefaultTimeVariationFacade.getInstance(
					(IMeterReadingCommunication) getBean("DefaultMeterReadingCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"));
		}
		else if ("DefaultJoinTicketingFacade".equals(beanName))
		{
			return (IJoinTicketingFacade) DefaultJoinTicketingFacade.getInstance(
					(IJoinTicketingPersistance) getBean("DefaultJoinTicketingPersistenceService"),
					(IJoinTicketingCommunication) getBean("DefaultJoinTicketingCommunicationService"),
					(IApplicationFacade) getBean("ApplicationFacade"), (VersionControl) getBean(VERSION_CONTROL));
		}
		else if ("DefaultJoinTicketingPersistenceService".equals(beanName))
		{
			return DefaultJoinTicketingPersistenceService.getPersistenceService(context);
		}
		else if ("DefaultJoinTicketingCommunicationService".equals(beanName))
		{
			return DefaultJoinTicketingCommunicationService.getCommunicationService(context);
		}
		else if (VERSION_CONTROL.equals(beanName))
		{
			return VersionControl.getInstance(context);
		}

		return null;
	}
}
