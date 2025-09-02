package com.example.lazyco.IntegrationTests;

import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {com.example.lazyco.backend.core.WebMVC.BackendWebConf.class})
public class TestIT {

  @Autowired private AppUserService appUserService;

  @Test
  void shouldRetryOnOptimisticLockConflict() throws Exception {
    AppUserDTO newData = new AppUserDTO();
    newData.setEmail("newEmail");
    newData.setPassword("newPassword");
    newData.setFirstName("newFirstName");
    newData.setLastName("newLastName");
    newData.setUserId("newUserId");

    appUserService.create(newData);

    AppUserDTO user1 = appUserService.getSingle(newData);

    // Run in two threads to cause a version conflict
    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future<?> f1 =
        executor.submit(
            () -> {
              user1.setPassword("newPassword");
              appUserService.update(user1);
            });

    Future<?> f2 =
        executor.submit(
            () -> {
              user1.setPassword("newPassword1");
              appUserService.update(user1);
            });

    f1.get();
    f2.get();

    executor.shutdown();
  }
}
