package io.todoee.dao.test.dao;

import io.todoee.dao.annotation.Select;
import io.todoee.dao.annotation.Table;
import io.todoee.dao.annotation.Update;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@Table
public interface UserDao2 {
	
	@Select("select * from user where id = #{id}")
	Future<JsonObject> get(String id);
	
	@Select("select * from user")
	Future<JsonArray> list();
	
	@Select("select count(1) from user")
	Future<Long> count();
	
	@Update("update user set nickname=#{nickname} where id=#{id}")
	Future<Integer> update(JsonObject param);

}
