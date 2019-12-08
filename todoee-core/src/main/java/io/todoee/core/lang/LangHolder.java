package io.todoee.core.lang;

/**
 * 
 * @author James.zhang
 *
 */
public class LangHolder {  
	  
    private static final ThreadLocal<String> langHolder = new ThreadLocal<String>();  
  
    public static void set(String customerType) {  
        langHolder.set(customerType);  
    }  
  
    public static String get() {  
        return langHolder.get();  
    }  
  
    public static void clear() {  
        langHolder.remove();  
    }  
}  
