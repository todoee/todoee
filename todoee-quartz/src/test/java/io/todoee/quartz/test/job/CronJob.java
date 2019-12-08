package io.todoee.quartz.test.job;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.todoee.quartz.annotation.Scheduled;
import io.todoee.quartz.test.biz.CronBiz;

@Scheduled(cron="0 43 10 * * ?")
public class CronJob implements Job {
	
	private CronBiz biz;
	
    @Inject
    public CronJob(CronBiz biz){
        this.biz = biz;
    }

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		biz.print();
	}
}
