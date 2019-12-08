package io.todoee.base.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验是否为空／不为空的工具类
 * @author James.zhang
 */
public class ObjUtil {

	/**
	 * empty object
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		return obj == null || isEmpty(obj.toString());
	}
	
	/**
	 * not empty object
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}
	
	/**
	 * empty String
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return StringUtils.isBlank(str);
	}
	
	/**
	 * not empty String
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}

	/**
	 * empty collection
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null) || (collection.isEmpty());
	}
	
	/**
	 * not empty collection
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	/**
	 * empty Iterator
	 * @param iterator
	 * @return
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return (iterator == null) || (iterator.hasNext());
	}
	
	/**
	 * not empty Iterator
	 * @param iterator
	 * @return
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return !isEmpty(iterator);
	}

	/**
	 * empty map
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null) || (map.isEmpty());
	}
	
	/**
	 * not empty map
	 * @param map
	 * @return
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
	/**
	 * empty Object[]
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty(Object[] objs) {
		return (objs == null) || (objs.length == 0);
	}
	
	/**
	 * not empty Object[]
	 * @param objs
	 * @return
	 */
	public static boolean isNotEmpty(Object[] objs) {
		return !isEmpty(objs);
	}
}
