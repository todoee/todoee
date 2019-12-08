package io.todoee.quartz.test.job;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Singleton;

import io.todoee.quartz.annotation.Scheduled;
import io.todoee.quartz.test.biz.SimpleBiz;

@Singleton
@Scheduled(interval=2, unit=TimeUnit.SECONDS)
public class SimpleJob implements Job {
	
	@Inject
	private SimpleBiz biz;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		biz.print();
	}
}
