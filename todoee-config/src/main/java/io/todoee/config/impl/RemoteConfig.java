package io.todoee.config.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.todoee.core.boot.CommandParam;
import io.todoee.core.config.ConfigType;
import io.todoee.core.config.DoConfig;
import io.todoee.core.context.IocContext;

/**
 * 
 * @author James.zhang
 *
 */
public class RemoteConfig extends DoConfig {

	private static final String CONFIG_DOMAIN_NAME = "config.center";
	private static final int CONFIG_PORT = 9091;
	
	private Config load(String type) {
		CommandParam param = IocContext.getBean(CommandParam.class);
		String serviceId = param.getServiceId();
		if (serviceId == null) {
			throw new RuntimeException("Remote config param sid is null");
		}
		String searchUrl = "http://" + CONFIG_DOMAIN_NAME + ":" + CONFIG_PORT + "/" + type + "?sid=" + serviceId;
		String newConfigStr = httpSend(searchUrl);
		Map<String, Object> data = getData(newConfigStr);
		Config newConfig = ConfigFactory.parseMap(data);
		return newConfig;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getData(String json) {
		Map<String, Object> map = JSON.parseObject(json, Map.class);
		return map;
	}

	private String httpSend(String searchUrl) {
		StringBuffer out = new StringBuffer();
		try {
			URL url = new URL(searchUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content, Charset.forName("UTF-8")));
			String line;
			while ((line = in.readLine()) != null) {
				out.append(line);
			}
			in.close();
			connection.disconnect();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return out.toString();
	}

	@Override
	protected Config loadTodoee() {
		return load(ConfigType.TODOEE);
	}

	@Override
	protected Config loadApplication() {
		return load(ConfigType.APPLICATION);
	}
}
