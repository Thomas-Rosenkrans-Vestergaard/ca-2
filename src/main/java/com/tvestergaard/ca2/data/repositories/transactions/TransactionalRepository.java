package com.tvestergaard.ca2.data.repositories.transactions;

import javax.persistence.EntityManager;

public interface TransactionalRepository extends Transactional
{

    /**
     * Returns the currently active entity manager instance.
     *
     * @return The currently active entity manager.
     */
    EntityManager getEntityManager();

    /**
     * Sets the currently active entity manager instance.
     *
     * @param entityManger The new active manager.
     */
    void setEntityManager(EntityManager entityManger);
}
