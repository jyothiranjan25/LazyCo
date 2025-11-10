package com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderByDTO;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderType;
import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobScheduleDTO;
import com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobScheduleService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CronJobExecutionLogService
    extends AbstractService<CronJobExecutionLogDTO, CronJobExecutionLog>
    implements ICronJobExecutionLogService {

  public CronJobExecutionLogService(CronJobExecutionLogMapper mapper) {
    super(mapper);
  }

  @Override
  protected void makeUpdates(CronJobExecutionLogDTO source, CronJobExecutionLog target) {
    if (source.getStatus() != null) target.setStatus(source.getStatus());
    if (source.getEndTime() != null) target.setEndTime(source.getEndTime());
    if (source.getRemarks() != null) target.setRemarks(source.getRemarks());
  }

  public CronJobExecutionLogDTO recordCronJobExecutionLog(long jobId) {
    // Get CronJob Schedule Dto
    CronJobScheduleDTO cronJobScheduleDTO = new CronJobScheduleDTO();
    cronJobScheduleDTO.setId(jobId);
    CronJobScheduleService cronJobScheduleService = getBean(CronJobScheduleService.class);
    cronJobScheduleDTO = cronJobScheduleService.getSingle(cronJobScheduleDTO);

    // Save CronJob Execution Log
    CronJobExecutionLogDTO dto = new CronJobExecutionLogDTO();
    dto.setCronJobScheduleId(cronJobScheduleDTO.getId());
    dto.setStatus(CronJobStatusEnum.IN_PROGRESS);
    dto.setUserModifiedGroup(cronJobScheduleDTO.getUserModifiedGroup());
    dto.setCreatedBy("SYSTEM");
    dto.setStartTime(new java.util.Date());

    return super.create(dto);
  }

  @Override
  protected void postUpdate(
      CronJobExecutionLogDTO dtoToUpdate,
      CronJobExecutionLog entityBeforeUpdate,
      CronJobExecutionLog updatedEntity) {
    if (updatedEntity.getStatus() == CronJobStatusEnum.FAILED) {
      checkLastXLogsStatus(getBean(CronJobExecutionLogMapper.class).map(updatedEntity));
    }
  }

  public void checkLastXLogsStatus(CronJobExecutionLogDTO cronjobExecutionLogDTO) {
    // Get CronJob Execution Log
    CronJobExecutionLogDTO dto = new CronJobExecutionLogDTO();
    dto.setCronJobScheduleId(cronjobExecutionLogDTO.getCronJobScheduleId());
    dto.setPageSize(cronjobExecutionLogDTO.getFailureLimit());
    OrderByDTO orderByDTO = new OrderByDTO("id", OrderType.DESC);
    dto.setOrderBy(List.of(orderByDTO));
    List<CronJobExecutionLogDTO> cronJobExecutionLogDTOs = get(dto);

    // check if all are failed using stream
    boolean allFailed =
        cronJobExecutionLogDTOs.stream()
            .allMatch(x -> x.getStatus().equals(CronJobStatusEnum.FAILED));

    // if all are failed then deactivate the cron job
    if (allFailed) {
      CronJobScheduleDTO cronJobScheduleDTO = new CronJobScheduleDTO();
      cronJobScheduleDTO.setId(cronjobExecutionLogDTO.getCronJobScheduleId());
      cronJobScheduleDTO.setStatus(false);
      CronJobScheduleService cronJobScheduleService = getBean(CronJobScheduleService.class);
      cronJobScheduleService.update(cronJobScheduleDTO);
    }
  }
}
