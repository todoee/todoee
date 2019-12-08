package io.todoee.service.ref.holder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ReferenceHolder {
	private static final String REFERENCE_FILE = "META-INF/references.sc";
	
	public static List<Class<?>> references() {
		try {
			return loadReferences(REFERENCE_FILE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static List<Class<?>> loadReferences(String fileResources) throws Exception {
    	List<Class<?>> packageList = new ArrayList<>();
    	Enumeration<URL> urls = ReferenceHolder.class.getClassLoader().getResources(fileResources);
    	while(urls.hasMoreElements()){
    		URL value = urls.nextElement();
    		log.debug("scan target reference file: " + value);
    		
    		InputStream fileIn = toInputStream(value);
    		
    		BufferedReader in = new BufferedReader(new InputStreamReader(fileIn));
        	String line = "";
        	while ((line = in.readLine()) != null){
        		packageList.add(toClass(line));
        		log.debug("found target reference: " + line);
        	}
        	in.close();
        }
    	return packageList;
    }

	private static InputStream toInputStream(URL value) throws IOException {
		InputStream fileIn = value.openStream();
		return fileIn;
	}
	
	private static Class<?> toClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}
}
