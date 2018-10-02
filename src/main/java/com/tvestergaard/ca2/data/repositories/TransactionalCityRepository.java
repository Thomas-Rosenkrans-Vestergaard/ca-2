package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
        return null;
    }
}
