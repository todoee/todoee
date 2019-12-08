package io.todoee.web.routing.handler.impl;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.todoee.web.constants.WebConstant;
import io.todoee.web.result.Payload;
import io.todoee.web.result.ResultArray;
import io.todoee.web.routing.handler.ReturnHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import com.alibaba.fastjson.JSON;

public class DefaultReturnHandler implements ReturnHandler {

	@Override
	public void handle(RoutingContext context) {
		HttpServerResponse response = context.response();
		Object returnObj = context.get(WebConstant.METHOD_RETURN_OBJ);
		
		String result;
		boolean payloadEnable = context.get(WebConstant.PAYLOAD_ENABLE);
		if (payloadEnable) {
			Payload payload = Payload.getInstance(returnObj);
			if (returnObj instanceof ResultArray) {
				ResultArray resultArray = (ResultArray)returnObj;
				payload.setTotal(resultArray.getTotal());
				payload.setData(resultArray.getItem());
			} else {
				payload.setData(returnObj);
			}
			String action = context.get("action");
			if (action != null) {
				payload.setAction(action);
			}
			result = JSON.toJSONString(payload);
		} else {
			result = JSON.toJSONString(returnObj);
		}
		response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		response.end(result);
	}
}
