package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule;

import static com.example.lazyco.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobMessages;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.List;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.springframework.stereotype.Component;

@Component
public class CronJobScheduleListener {
  @PrePersist
  private void prePersist(CronJobSchedule entity) {
    CronJobScheduleService service = getBean(CronJobScheduleService.class);
    CronJobScheduleDTO filter = new CronJobScheduleDTO();
    filter.setCronJobType(entity.getCronJobType());
    List<CronJobScheduleDTO> dtos = service.get(filter);
    // check if the record already exists
    if (dtos != null && !dtos.isEmpty()) {
      throw new ApplicationException(CommonMessage.DUPLICATE_RECORD);
    }
    // validate cron expression
    isValidCronExpression(entity.getCronExpression());
    if (entity.getStatus() == null) entity.setStatus(true);
  }

  @PostPersist
  private void postPersist(CronJobSchedule entity) {
    CronJobService cronJobService = getBean(CronJobService.class);

    CronJobScheduleDTO cronJobSchedule = getBean(CronJobScheduleMapper.class).map(entity);
    if (entity.getStatus()) {
      Class<? extends Job> jobClass = cronJobSchedule.getCronJobType().getCronJobType();
      cronJobService.addCronJob(jobClass, cronJobSchedule);
    }
  }

  @PreUpdate
  private void preUpdate(CronJobSchedule entity) {
    CronJobService cronJobService = getBean(CronJobService.class);
    // validate cron expression
    isValidCronExpression(entity.getCronExpression());
    CronJobScheduleDTO cronJobSchedule = getBean(CronJobScheduleMapper.class).map(entity);
    if (entity.getStatus() != null && !entity.getStatus()) {
      // remove the cron job from the scheduler...
      cronJobService.removeCronJob(cronJobSchedule);
    } else {
      // add the cron job to the scheduler...
      Class<? extends Job> jobClass = cronJobSchedule.getCronJobType().getCronJobType();
      cronJobService.addCronJob(jobClass, cronJobSchedule);
    }
  }

  public boolean isValidCronExpression(String cronExpression) {
    if (!CronExpression.isValidExpression(cronExpression)) {
      throw new ApplicationException(
          CronJobMessages.INVALID_CRON_EXPRESSION, new Object[] {cronExpression});
    }
    return true;
  }
}
