package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.BatchJob.SpringBatch.AbstractBatchJob;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@Service
public class AppUserUploader extends AbstractBatchJob<AppUserDTO, AppUserDTO> {

  private final AppUserService appUserService;

  public AppUserUploader(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  protected ItemProcessor<AppUserDTO, AppUserDTO> createItemProcessor() {
    return item -> {
      // Here you can implement any processing logic if needed
      return item;
    };
  }

  @Override
  protected ItemWriter<AppUserDTO> createItemWriter() {
    return items -> items.forEach(this::createTest);
  }

  private void createTest(AppUserDTO dto) {
    ApplicationLogger.info("Creating AppUser: " + dto);
  }
}
