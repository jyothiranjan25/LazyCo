package com.example.lazyco.backend.core.AbstractClasses.ServiceComponents;

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

  /**
   * Execute create operation with transaction boundary. Implement this method in concrete service
   * classes to define create logic.
   *
   * @param dto The DTO containing data to create
   * @return The created DTO with populated fields
   */
  D executeCreateTransactional(D dto);

  /**
   * Execute create operation with a new transaction. Implement this method in concrete service
   * classes to define create logic that must not be affected by existing transactions.
   *
   * @param dto The DTO containing data to create
   * @return The created DTO with populated fields
   */
  D executeCreateNewTransactional(D dto);

  /**
   * Execute create operation within a nested transaction. Implement this method in concrete service
   * classes to define create logic that can be rolled back independently.
   *
   * @param dto The DTO containing data to create
   * @return The created DTO with populated fields
   */
  D executeCreateNestedTransactional(D dto);

  /**
   * Execute update operation with transaction boundary. Implement this method in concrete service
   * classes to define update logic.
   *
   * @param dto The DTO containing data to update
   * @return The updated DTO
   */
  D executeUpdateTransactional(D dto);

  /**
   * Execute update operation with a new transaction. Implement this method in concrete service
   * classes to define update logic that must not be affected by existing transactions.
   *
   * @param dto The DTO containing data to update
   * @return The updated DTO
   */
  D executeUpdateNewTransactional(D dto);

  /**
   * Execute update operation within a nested transaction. Implement this method in concrete service
   * classes to define update logic that can be rolled back independently.
   *
   * @param dto The DTO containing data to update
   * @return The updated DTO
   */
  D executeUpdateNestedTransactional(D dto);

  /**
   * Execute delete operation with transaction boundary. Implement this method in concrete service
   * classes to define delete logic.
   *
   * @param dto The DTO containing data to delete
   * @return The deleted DTO
   */
  D executeDeleteTransactional(D dto);

  /**
   * Execute delete operation with a new transaction. Implement this method in concrete service
   * classes to define delete logic that must not be affected by existing transactions.
   *
   * @param dto The DTO containing data to delete
   * @return The deleted DTO
   */
  D executeDeleteNewTransactional(D dto);

  /**
   * Execute delete operation within a nested transaction. Implement this method in concrete service
   * classes to define delete logic that can be rolled back independently.
   *
   * @param dto The DTO containing data to delete
   * @return The deleted DTO
   */
  D executeDeleteNestedTransactional(D dto);
}
