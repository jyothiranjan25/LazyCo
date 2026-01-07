package com.example.lazyco.backend.entities.UserManagement.AppUser.Upload;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractModelMapper;
import com.example.lazyco.core.BatchJob.BatchJobOperationType;
import com.example.lazyco.core.BatchJob.SpringBatch.AbstractBatchJob;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
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
public class AppUserUploader extends AbstractBatchJob<AppUserBatchDTO, AppUserDTO> {

  private final AppUserService appUserService;

  public AppUserUploader(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  protected ItemProcessor<@NonNull AppUserBatchDTO, @NonNull AppUserDTO> createItemProcessor(
      BatchJobOperationType operationType, Map<Class<?>, List<?>> childData) {
    return item -> {
      return new AbstractModelMapper().map(item, AppUserDTO.class);
    };
  }

  @Override
  protected ItemWriter<@NonNull AppUserDTO> createItemWriter(BatchJobOperationType operationType) {
    return items -> items.forEach(this::createTest);
  }

  private void createTest(AppUserDTO dto) {
    appUserService.executeCreateNestedTransactional(dto);
  }
}
