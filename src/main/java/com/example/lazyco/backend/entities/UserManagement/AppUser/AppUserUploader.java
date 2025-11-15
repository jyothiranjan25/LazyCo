package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.BatchJob.SpringBatch.AbstractBatchJob;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

/**
 * AppUserUploader is a batch job service that processes and uploads AppUserDTO objects. It extends
 * AbstractBatchJob to leverage batch processing capabilities.
 *
 * @see AbstractBatchJob
 * @see AppUserDTO
 * @see AppUserService use @Scope("prototype") at service if you want a new instance each time else
 *     use applicationContect.getBean(AppUserUploader.class) to get a new instance each time
 */
@Service
public class AppUserUploader extends AbstractBatchJob<AppUserDTO, AppUserDTO> {

  private final AppUserService appUserService;

  public AppUserUploader(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  protected ItemProcessor<AppUserDTO, AppUserDTO> createItemProcessor() {
    return item -> {
      ApplicationLogger.info("AppUserUploader: Processing AppUserDTO");
      // Here you can implement any processing logic if needed
      return item;
    };
  }

  @Override
  protected ItemWriter<AppUserDTO> createItemWriter() {
    return items -> items.forEach(this::createTest);
  }

  private void createTest(AppUserDTO dto) {
    appUserService.create(dto);
  }
}
