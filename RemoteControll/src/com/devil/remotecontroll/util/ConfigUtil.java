package com.devil.remotecontroll.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.devil.remotecontroll.BaseApplication;

import android.content.Context;

public class ConfigUtil {
	private final static String APP_CONFIG = "config";
	
	public final static String CONF_IP_ADDRESS = "perf_ip_adress";

	private Context context = BaseApplication.getInstance();

	private static ConfigUtil config;

	private ConfigUtil(){
		
	}
	/**
	 * 单例获config对象
	 * 
	 * @param context
	 * @return
	 */
	public static ConfigUtil getConfig() {
		if(config==null){
			synchronized (ConfigUtil.class) {
				config = new ConfigUtil();
			}
		}
		return config;
	}

	/**
	 * 保存到配置集合对象
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		Properties properties = get();
		properties.setProperty(key, value);
		setProps(properties);
	}
	/**
	 * 保存到配置集合对象
	 * 
	 * @param ps
	 */
	public void set(Properties ps) {
		Properties properties = get();
		properties.putAll(ps);
		setProps(properties);
	}

	public void remove(String... key) {
		Properties properties = get();
		for (String k : key) {
			properties.remove(k);
		}
		setProps(properties);
	}

	/**
	 * key取value
	 * 
	 * @param key
	 * @return 如果有配置问价 返回value 否则返回null
	 */
	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	/**
	 * 获取配置集合对象
	 * 
	 * @return
	 */
	public Properties get() {
		FileInputStream fis = null;
		Properties properties = new Properties();
		try {
			File dirConf = context.getCacheDir();

			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ APP_CONFIG);
			properties.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	/**
	 * 设置配置集合对象
	 * 
	 * @return
	 */
	public Properties setProps(Properties properties) {
		FileOutputStream fos = null;
		try {
			File dirConf = context.getCacheDir();

			File conf = new File(dirConf, APP_CONFIG);

			fos = new FileOutputStream(conf);

			properties.store(fos, null);

			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
