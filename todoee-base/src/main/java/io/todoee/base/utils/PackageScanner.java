package io.todoee.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.reflections.Reflections;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class PackageScanner {
	private static final String PACKAGE_SCANNER = "META-INF/packages.sc";

	private static Reflections reflections;

	public static Reflections getReflections() {
		return reflections;
	}
	
	static {
		Object[] packages = new Object[0];
		try {
			packages = loadPackages(PACKAGE_SCANNER);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		reflections = new Reflections(packages);
	}

	public static Set<Class<?>> scanAnnotation(Class<? extends Annotation> annotationClass) {
		Set<Class<?>> targets = reflections.getTypesAnnotatedWith(annotationClass);

		if (targets.size() == 0) {
			log.debug("scan annotation class count is 0");
			return Collections.emptySet();
		} else {
			return targets;
		}
	}

	public static Set<?> scanSubType(Class<?> type) {
		Set<?> subTypes = reflections.getSubTypesOf(type);
		if (subTypes.size() == 0) {
			log.debug("scan subtype class count is 0");
			return Collections.emptySet();
		} else {
			return subTypes;
		}
	}

	private static Object[] loadPackages(String fileResources) throws Exception {
		List<String> packageList = new ArrayList<>();
		Enumeration<URL> urls = PackageScanner.class.getClassLoader().getResources(fileResources);
		while (urls.hasMoreElements()) {
			URL value = urls.nextElement();
			log.debug("scan target export: " + value);

			InputStream fileIn;
			if (value.toString().startsWith("jar:file")) {
				JarURLConnection jarConnection = (JarURLConnection) value.openConnection();
				fileIn = jarConnection.getInputStream();
			} else {
				fileIn = new FileInputStream(new File(value.toURI()));
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(fileIn));
			String line = "";
			while ((line = in.readLine()) != null) {
				packageList.add(line);
				log.debug("found target package: " + line);
			}
			in.close();
		}
		return packageList.toArray();
	}
}
