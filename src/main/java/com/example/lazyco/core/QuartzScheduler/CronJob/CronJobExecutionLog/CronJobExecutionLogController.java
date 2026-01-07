package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cron_job_execution_log")
public class CronJobExecutionLogController extends AbstractController<CronJobExecutionLogDTO> {
  public CronJobExecutionLogController(ICronJobExecutionLogService abstractService) {
    super(abstractService);
  }
}
