package io.todoee.quartz.verticle;

import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import io.todoee.base.utils.PackageScanner;
import io.todoee.core.context.IocContext;
import io.todoee.quartz.annotation.Scheduled;
import io.todoee.quartz.factory.GuiceJobFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 * 
 */
@Slf4j
public class QuartzVerticle extends AbstractVerticle {

	private Scheduler scheduler;
	
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		initScheduler();
	}

	private void initScheduler() {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/quartz.properties");
		StdSchedulerFactory fact = new StdSchedulerFactory();
		try {
			if (is != null) {
				log.info("use quartz.properties init");
				fact.initialize(is);
			}
			scheduler = fact.getScheduler();
			scheduler.setJobFactory(IocContext.getBean(GuiceJobFactory.class));
			scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void start() throws Exception {
		log.info("start quartz scheduler...");
		Set<Class<? extends Job>> jobs = PackageScanner.getReflections().getSubTypesOf(Job.class);
		jobs.forEach(clazz -> {
			log.debug("loaded job class: "+ clazz.getName());
            JobDetail job = JobBuilder.newJob(clazz).build();
            Scheduled scheduled = clazz.getAnnotation(Scheduled.class);
            Trigger trigger = buildTrigger(scheduled);
            try {
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		});
		log.info("started quartz scheduler");
	}
	
	private Trigger buildTrigger(Scheduled ann) {
		if (ann == null) {
			throw new IllegalArgumentException("@Scheduled is required for the Job Class");
		}
		
		log.debug("build trigger");
		TriggerBuilder<Trigger> trigger = TriggerBuilder.newTrigger();

		if (ann.cron() != null && ann.cron().trim().length() > 0) {
			trigger.withSchedule(CronScheduleBuilder.cronSchedule(ann.cron()));
		} else if (ann.interval() != -1) {
			trigger.withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(
                            TimeUnit.MILLISECONDS.convert(ann.interval(), ann.unit()))
                    .repeatForever())
                    .startAt(new Date(System.currentTimeMillis() + ann.delayInMillis()));
		} else {
			throw new IllegalArgumentException("One of 'cron', 'interval' is required for the @Scheduled");
		}

		return trigger.build();
	}
	
	@Override
	public void stop() throws Exception {
		scheduler.shutdown();
	}

}
