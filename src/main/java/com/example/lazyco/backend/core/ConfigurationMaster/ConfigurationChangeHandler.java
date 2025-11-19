package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ConfigurationChangeHandler {

  private final AbstractAction abstractAction;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void reloadConfigs(ConfigurationMaster event) {
    abstractAction.initializeSystemProps();
  }
}
