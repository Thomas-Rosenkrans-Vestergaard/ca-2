package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Allows for retrieval and other operations on city entities.
 */
public class TransactionalCityRepository extends TransactionalCrudRepository<City, Integer> implements CityRepository
{

    public TransactionalCityRepository(EntityManager entityManager)
    {
        super(entityManager, City.class);
    }

    public TransactionalCityRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, City.class);
    }

    public TransactionalCityRepository()
    {
        super(City.class);
    }

    @Override public List<String> getZipCodes()
    {
        return getEntityManager().createQuery("SELECT c.zipCode FROM City c", String.class).getResultList();
    }

    @Override public City withZipCode(String zipCode)
    {
        try {
            return getEntityManager().createQuery("SELECT c FROM City c WHERE c.zipCode = :zipCode", City.class)
                                     .setParameter("zipCode", zipCode)
                                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
