package io.todoee.web.result;

import java.util.Collection;

import lombok.ToString;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author James.zhang
 *
 */
@ToString
public class ResultArray {
	
	private JSONArray item = new JSONArray();
	
	/**
	 * 分页查询中，返回总条数.
	 */
	private Long total;
	
	/**
	 * <p>Description:[构造器方法描述].</p> []
	 * @constructor 方法.
	 */
	public ResultArray(){}
	
	/**
	 * <p>Description:[构造器方法描述].</p>
	 * @param total 总条数
	 * @param object 实体(list)
	 * @constructor 方法.
	 */
	public ResultArray(Long total, Collection<? extends Object> array){
		this.item.addAll(array);
		this.total = total;
	}

	public void add(Object entity) {
		this.item.add(entity);
	}

	public void add(Collection<? extends Object> array) {
		this.item.addAll(array);
	}
	
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public JSONArray getItem() {
		return item;
	}
}
