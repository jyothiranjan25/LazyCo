package com.example.lazyco.backend.core.QuartzScheduler.CronJob;

import static org.quartz.JobBuilder.newJob;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobScheduleDTO;
import com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobScheduleService;
import jakarta.annotation.PreDestroy;
import java.util.List;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CronJobService {

  private Scheduler scheduler;
  private CronJobScheduleService cronJobScheduleService;
  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(
      Scheduler scheduler,
      CronJobScheduleService cronJobScheduleService,
      AbstractAction abstractAction) {
    this.scheduler = scheduler;
    this.cronJobScheduleService = cronJobScheduleService;
    this.abstractAction = abstractAction;
  }

  public static String getGroupId(Long cronJobScheduleId) {
    return "group" + cronJobScheduleId;
  }

  public static String getJobId(Long cronJobScheduleId) {
    return "job_" + cronJobScheduleId;
  }

  public static String getTriggerId(Long cronJobScheduleId) {
    return "trigger" + cronJobScheduleId;
  }

  public static JobKey getJobKey(Long cronJobScheduleId) {
    return JobKey.jobKey(getJobId(cronJobScheduleId), getGroupId(cronJobScheduleId));
  }

  public static TriggerKey getTriggerKey(Long cronJobScheduleId) {
    return TriggerKey.triggerKey(getTriggerId(cronJobScheduleId), getGroupId(cronJobScheduleId));
  }

  public void addCronJob(Class<? extends Job> jobClass, CronJobScheduleDTO cronJobSchedule) {
    try {
      // define the job and tie it to our Job class
      JobDetail job = newJob(jobClass).withIdentity(getJobKey(cronJobSchedule.getId())).build();

      // Trigger the job to run
      Trigger trigger =
          TriggerBuilder.newTrigger()
              .withIdentity(getTriggerKey(cronJobSchedule.getId()))
              .withSchedule(CronScheduleBuilder.cronSchedule(cronJobSchedule.getCronExpression()))
              .build();

      // Tell quartz to schedule the job using our trigger
      scheduler.scheduleJob(job, trigger);
    } catch (Throwable e) {
      ApplicationLogger.error(e, e.getClass());
    }
  }

  public void removeCronJob(CronJobScheduleDTO cronJobSchedule) {
    try {
      scheduler.deleteJob(
          new JobKey(getJobId(cronJobSchedule.getId()), getGroupId(cronJobSchedule.getId())));
    } catch (Throwable e) {
      ApplicationLogger.error(e, e.getClass());
    }
  }

  /** method to run at the startup, that will start all the active cronJobSchedules */
  @EventListener(ContextRefreshedEvent.class)
  public void startAllActiveCronJobs() {
    abstractAction.setBypassRBAC(true);
    try {
      // get all the active cronJobSchedules
      CronJobScheduleDTO cronJobScheduleDTO = new CronJobScheduleDTO();
      cronJobScheduleDTO.setStatus(true);

      // get the cronJobScheduleDTOs
      List<CronJobScheduleDTO> cronJobScheduleDTOs = cronJobScheduleService.get(cronJobScheduleDTO);

      ApplicationLogger.info("Started cron jobs: " + cronJobScheduleDTOs.size());
      // loop through the cronJobScheduleDTOs and add the cron jobs
      for (CronJobScheduleDTO cronJobSchedule : cronJobScheduleDTOs) {
        CronJobTypeEnum cronJobType = cronJobSchedule.getCronJobType();
        Class<? extends Job> jobClass = cronJobType.getCronJobType();
        addCronJob(jobClass, cronJobSchedule);
      }
    } finally {
      abstractAction.setBypassRBAC(false);
    }
  }

  /** method to run at the shutdown, that will stop all the active cronJobSchedules */
  @PreDestroy
  public void shutdown() {
    try {
      if (scheduler != null && !scheduler.isShutdown()) {
        scheduler.shutdown(true);
      }
      ApplicationLogger.info("Scheduler manually shut down.");

      if (scheduler != null && scheduler.isShutdown()) {
        ApplicationLogger.info("Scheduler is shutdown.");
      } else {
        ApplicationLogger.warn("Scheduler is not shutdown.");
      }
    } catch (SchedulerException e) {
      ApplicationLogger.error(e, e.getClass());
    }
  }
}
