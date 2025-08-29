package com.example.lazyco.backend.core.AbstractClasses.Service.ServiceComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;

/**
 * Transaction propagation quick guide:
 *
 * <p>- REQUIRED (default) → Join current tx, or start new if none. (all-or-nothing)
 *
 * <p>- REQUIRES_NEW → Always start a new tx, suspends parent. (use for independent ops like
 * logs/audit)
 *
 * <p>- SUPPORTS → Join tx if present, else run non-transactional. (good for read-only queries)
 *
 * <p>- NESTED → Run in current tx with a savepoint; rollback affects only nested part.
 * (fine-grained rollback)
 *
 * <p>- MANDATORY → Must have a tx, else throws. (enforces caller-managed tx)
 *
 * <p>- NOT_SUPPORTED → Always run non-transactional, suspends parent. (expensive reads, no locking)
 *
 * <p>- NEVER → Must not run in a tx, else throws. (rare; external API calls)
 */
public interface TransactionalService<D extends AbstractDTO<D>> {

  // Required Transaction
  D executeCreateTransactional(D dto);

  // Requires New Transaction
  D executeCreateNewTransactional(D dto);

  // Nested Transaction
  D executeCreateNestedTransactional(D dto);

  // Required Transaction
  D executeUpdateTransactional(D dto);

  // Requires New Transaction
  D executeUpdateNewTransactional(D dto);

  // Nested Transaction
  D executeUpdateNestedTransactional(D dto);

  // Required Transaction
  D executeDeleteTransactional(D dto);

  // Requires New Transaction
  D executeDeleteNewTransactional(D dto);

  // Nested Transaction
  D executeDeleteNestedTransactional(D dto);
}
