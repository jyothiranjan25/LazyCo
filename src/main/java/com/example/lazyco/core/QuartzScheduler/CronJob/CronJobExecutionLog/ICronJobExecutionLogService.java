package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;

public interface ICronJobExecutionLogService
    extends IAbstractService<CronJobExecutionLogDTO, CronJobExecutionLog> {
  CronJobExecutionLogDTO recordCronJobExecutionLog(long jobId);

  void checkLastXLogsStatus(CronJobExecutionLogDTO entity);
}
