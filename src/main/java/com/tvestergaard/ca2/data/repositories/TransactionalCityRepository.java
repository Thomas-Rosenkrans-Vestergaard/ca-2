package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.City;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Allows for retrieval and other operations on city entities.
 */
public class TransactionalCityRepository extends TransactionalCrudRepository<City, Integer> implements CityRepository
{

    /**
     * Creates a new {@link TransactionalCityRepository}.
     *
     * @param entityManagerFactory The entity manager factory that the repository acts upon.
     */
    public TransactionalCityRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, City.class);
    }

    /**
     * Returns a complete list of the zip codes in the system.
     *
     * @return The list of the zip codes in the system.
     */
    @Override public List<String> getZipCodes()
    {
        return null;
    }
}
