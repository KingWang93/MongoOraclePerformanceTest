package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;


public class PropertiesUtil
{
	/* 根据键和属性表的表名获取相应的值
	 * @para   propertyname   配置文件名
	 * 		   sKey  		     键的名字
	 */
	public static String getProperties(String propertyname,String sKey)
	{
		String url=Thread.currentThread().getContextClassLoader()
				.getResource("").toString();
		url=url.substring(url.indexOf("/")+1);
		url=url.replaceAll("%20"," ");
		Properties properties=new Properties();
		try
		{
			InputStreamReader insReader=new InputStreamReader(
					new FileInputStream(url+propertyname+".properties"),"UTF-8");
			properties.load(insReader);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		String s=properties.getProperty(sKey);
		return s;
	}
	/* 获取属性表的全部内容
	 * @para   propertyname   配置文件名
	 */
	public static String getProperties(String propertyname)
	{
		String url=Thread.currentThread().getContextClassLoader()
				.getResource("").toString();
		url=url.substring(url.indexOf("/")+1);
		url=url.replaceAll("%20"," ");
		Properties properties=new Properties();
		try
		{
			InputStreamReader insReader=new InputStreamReader(
					new FileInputStream(url+propertyname+".properties"),"UTF-8");
			properties.load(insReader);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		String s=properties.toString();
		return s;
	}
	/* 获取属性表的键值的集合
	 * @para   propertyname   配置文件名
	 */
	public static Set<Object> getKeyValue(String propertyname)
	{
		String url=Thread.currentThread().getContextClassLoader().getResource("").toString();
		url=url.substring(url.indexOf("/")+1);
		url=url.replaceAll("%20"," ");
		Properties properties=new Properties();
		try
		{
			InputStreamReader insReader=new InputStreamReader(new FileInputStream(url+propertyname+".properties"),"UTF-8");
			properties.load(insReader);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		Set<Object> keyValue=properties.keySet();
		return keyValue;
	}

}