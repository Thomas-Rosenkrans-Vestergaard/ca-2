package com.tvestergaard.ca2.data.repositories.transactions;

import com.tvestergaard.ca2.data.repositories.CloseStrategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class AbstractTransactionalRepository implements TransactionalRepository
{

    private final EntityManagerFactory entityManagerFactory;
    private       EntityManager        entityManager;
    private       CloseStrategy        closeStrategy = CloseStrategy.COMMIT;
    private       boolean              isComplete    = true;

    public AbstractTransactionalRepository(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.begin();
    }

    @Override public EntityManager getEntityManager()
    {
        if (entityManager == null)
            throw new NoActiveEntityManagerException();

        return entityManager;
    }

    @Override public AbstractTransactionalRepository begin()
    {
        if (entityManager != null) {
            if (!isComplete) {
                if (closeStrategy == CloseStrategy.COMMIT) {
                    entityManager.getTransaction().commit();
                } else if (closeStrategy == CloseStrategy.ROLLBACK) {
                    entityManager.getTransaction().rollback();
                } else {
                    throw new UnsupportedOperationException("Unsupported CloseStrategy " + closeStrategy.name());
                }

                entityManager.close();
            }

            return this;
        }

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        isComplete = false;
        return this;
    }

    @Override public AbstractTransactionalRepository commit()
    {
        if (entityManager == null)
            throw new NoActiveEntityManagerException();

        if (isComplete)
            throw new NoActiveTransactionException();

        entityManager.getTransaction().commit();
        entityManager.close();
        begin();
        return this;
    }

    @Override public AbstractTransactionalRepository rollback()
    {

        if (entityManager == null)
            throw new NoActiveEntityManagerException();

        if (isComplete)
            throw new NoActiveTransactionException();

        entityManager.getTransaction().rollback();
        entityManager.close();
        begin();
        return this;
    }

    @Override public AbstractTransactionalRepository onClose(CloseStrategy strategy)
    {
        this.closeStrategy = strategy;
        return this;
    }

    @Override public CloseStrategy getCloseStrategy()
    {
        return this.closeStrategy;
    }

    @Override public EntityTransaction getTransaction()
    {
        if (entityManager == null)
            throw new NoActiveTransactionException();

        return entityManager.getTransaction();
    }

    @Override public EntityManager newEntityManager()
    {
        return entityManagerFactory.createEntityManager();
    }
}
