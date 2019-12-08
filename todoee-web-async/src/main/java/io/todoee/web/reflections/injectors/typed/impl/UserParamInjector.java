package io.todoee.web.reflections.injectors.typed.impl;
//package com.skycloud.skystack.rest.reflections.injectors.typed.impl;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.skycloud.skystack.rest.reflections.injectors.typed.ParamInjector;
//
//import io.vertx.ext.auth.User;
//import io.vertx.ext.web.RoutingContext;
//
//public class UserParamInjector implements ParamInjector<User> {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(UserParamInjector.class);
//
//	@Override
//	public User resolve(RoutingContext context) {
//		User user = context.user();
//		if (user == null) {
//			LOGGER.warn("Auth user not found in RoutingContext, need login or authentication");
//		}
//		return user;
//	}
//
//}
