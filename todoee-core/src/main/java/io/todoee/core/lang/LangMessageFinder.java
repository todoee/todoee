package io.todoee.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Singleton
@Slf4j
public class LangMessageFinder {

	private static final String PROPERTIES_DIR = "META-INF/lang";

	private final List<String> cachedFilenames = new ArrayList<>();

	private final ConcurrentMap<String, Properties> cachedProperties = new ConcurrentHashMap<>();

	private final ConcurrentMap<Locale, Properties> cachedMergedProperties = new ConcurrentHashMap<>();

	public LangMessageFinder() {
		try {
			loadPorperties();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getMessage(int code, Object... objs) {
		Locale locale = Locale.SIMPLIFIED_CHINESE;
		String lang = LangHolder.get();
		if (lang != null && !lang.trim().equals("")) {
			locale = getLocale(lang);
			LangHolder.clear();
		}
		Properties properties = getMergedProperties(locale);
		String value = properties.getProperty(code + "", "not found value");
		value = value.replaceAll("{}", "%s");
		String message = String.format(value, objs);
		return message;
	}

	private Properties getMergedProperties(Locale locale) {
		Properties merged = this.cachedMergedProperties.get(locale);
		if (merged != null) {
			return merged;
		}
		merged = new Properties();
		for (String basename : cachedFilenames) {
			String filename = calculateFilenamesForLocale(basename, locale);
			Properties properties = getProperties(filename);
			if (properties != null) {
				merged.putAll(properties);
			}
		}

		Properties existing = this.cachedMergedProperties.putIfAbsent(locale, merged);
		if (existing != null) {
			merged = existing;
		}
		return merged;
	}

	private Properties getProperties(String filename) {
		return this.cachedProperties.get(filename);
	}

	private String calculateFilenamesForLocale(String basename, Locale locale) {
		String language = locale.getLanguage();
		StringBuilder temp = new StringBuilder(basename);
		temp.append('_');
		temp.append(language);
		temp.append(".properties");
		return temp.toString();
	}

	private Locale getLocale(String lang) {
		String[] langs = lang.split("_");
		String language = langs[0];
		String country = langs[1];
		return new Locale(language, country);
	}

	private void loadPorperties() throws Exception {
		Enumeration<URL> urls = this.getClass().getClassLoader().getResources(PROPERTIES_DIR);
		while (urls.hasMoreElements()) {
			URL value = urls.nextElement();
			log.debug("scan target properties: " + value);
			initFilenames(value);
			initProperties(value);
		}
	}

	private void initProperties(URL value) throws IOException {
		Properties properties = new Properties();
		InputStream in = value.openConnection().getInputStream();
		properties.load(in);
		this.cachedProperties.putIfAbsent(value.getPath(), properties);
	}

	private void initFilenames(URL value) {
		String file = value.toString();
		file = file.substring(0, file.lastIndexOf("_"));
		this.cachedFilenames.add(file);
	}
}
