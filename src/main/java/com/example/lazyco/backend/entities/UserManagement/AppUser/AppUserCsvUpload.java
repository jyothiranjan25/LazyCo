package com.example.lazyco.backend.entities.UserManagement.AppUser;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.BatchJob.SpringBatch.AbstractSpringBatchJob;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@Service
public class AppUserCsvUpload extends AbstractSpringBatchJob<AppUserDTO, AppUserDTO> {

  public AppUserCsvUpload() {
    super("AppUserCsvUpload");
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
    return items ->
        items.forEach(
            item -> {
              // Here you can implement the logic to save the AppUserDTO to the database
              getBean(AppUserService.class).create(item);
            });
  }

  @Override
  public void executeJob(List<AppUserDTO> inputData, String batchJobName) {
    super.executeJob(inputData, batchJobName);
  }
}
