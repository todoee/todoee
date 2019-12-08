package io.todoee.quartz.test.biz;

import com.google.inject.Singleton;

@Singleton
public class CronBiz {
	public void print() {
		System.out.println("cronbiz hello world");
	}
}
