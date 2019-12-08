package io.todoee.quartz.test.biz;

import com.google.inject.Singleton;

@Singleton
public class SimpleBiz {
	public void print() {
		System.out.println("simplebiz hello world: " + Thread.currentThread().getName());
	}
}
