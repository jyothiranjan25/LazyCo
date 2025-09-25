package com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cron_job_schedule")
public class CronJobScheduleController extends AbstractController<CronJobScheduleDTO> {
  public CronJobScheduleController(AbstractService<CronJobScheduleDTO, ?> abstractService) {
    super(abstractService);
  }
}
