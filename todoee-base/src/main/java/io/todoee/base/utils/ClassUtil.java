package io.todoee.base.utils;

/**
 * 
 * @author James.zhang
 *
 */
public class ClassUtil {
	
	/**
     * 判断一个类是JAVA类型还是用户定义类型
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {   
        return clz != null && clz.getClassLoader() == null;   
    }
    
	public static boolean isPrimitive(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		} else if (isWrapClass(clazz)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isWrapClass(Class<?> clazz) {
		try {
			return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
}
