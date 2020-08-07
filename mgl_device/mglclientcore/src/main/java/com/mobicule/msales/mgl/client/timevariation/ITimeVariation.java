/**
 * 
 */
package com.mobicule.msales.mgl.client.timevariation;

/**
 * @author nikita
 *
 */
public interface ITimeVariation
{
	static final String KEY_DEVICE_TIMELIST = "time";

	static final String KEY_DEVICE_DATELIST = "date";

	public void init();

	public void reset();

	public Object toJSON();

	public void setDeviceDate(String deviceDate);

	public String getDeviceDate();

	public void setDeviceTime(String deviceTime);

	public String getDeviceTime();

}
