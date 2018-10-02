package com.tvestergaard.ca2.data.repositories.transactions;

import com.tvestergaard.ca2.data.repositories.CloseStrategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Supplier;

public interface TransactionalRepository
{

    /**
     * Begins a new transaction with a new {@code EntityManager}.
     *
     * @return this
     */
    TransactionalRepository begin();

    /**
     * Commits the current transaction.
     *
     * @return this
     * @throws NoActiveEntityManagerException When no entity manager exists.
     * @throws NoActiveTransactionException   When no active transaction exists.
     * @see TransactionalRepository#begin()
     */
    TransactionalRepository commit();

    /**
     * Executes the provided runnable within a new transaction. The transaction is then committed and closed.
     *
     * @param runnable The code to run within the new transaction.
     * @return this
     */
    default TransactionalRepository commit(Runnable runnable)
    {
        EntityManager entityManager = getEntityManager();

        try {
            runnable.run();
            entityManager.getTransaction().commit();
            return this;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            begin();
        }
    }

    /**
     * Executes the provided runnable within a new transaction. The transaction is then committed and closed.
     *
     * @param supplier The code to run within the new transaction.
     * @return this
     */
    default <T> T commit(Supplier<T> supplier)
    {
        EntityManager entityManager = getEntityManager();

        try {
            T result = supplier.get();
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
            begin();
        }
    }

    /**
     * Rolls back the current transaction.
     *
     * @return this
     * @throws NoActiveEntityManagerException When no entity manager exists.
     * @throws NoActiveTransactionException   When no active transaction exists.
     * @see TransactionalRepository#begin()
     */
    TransactionalRepository rollback();

    /**
     * Sets the operation to perform when {@link TransactionalRepository#begin()} is called without first calling
     * {@link TransactionalRepository#begin()} or {@link TransactionalRepository#rollback()}.
     *
     * @param strategy The strategy.
     * @return this
     */
    TransactionalRepository onClose(CloseStrategy strategy);

    /**
     * Returns the operation to perform when {@link TransactionalRepository#begin()} is called without first calling
     * {@link TransactionalRepository#begin()} or {@link TransactionalRepository#rollback()}.
     *
     * @return The strategy
     */
    CloseStrategy getCloseStrategy();

    /**
     * Returns the currently active entity manager instance.
     *
     * @return The currently active entity manager.
     * @throws NoActiveEntityManagerException When no active entity manager instance exists.
     * @see TransactionalRepository#begin()
     */
    EntityManager getEntityManager();

    /**
     * Creates a new entity manager from the registered entity manager factory.
     *
     * @return The newly created entity manager.
     */
    EntityManager newEntityManager();

    /**
     * Returns the currently active transaction from the currently active entity manager.
     *
     * @return the currently active transaction from the currently active entity manager.
     * @throws NoActiveEntityManagerException When no active entity manager instance exists.
     * @see TransactionalRepository#begin()
     */
    EntityTransaction getTransaction();
}
