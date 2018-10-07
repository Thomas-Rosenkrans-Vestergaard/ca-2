package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class TransactionalAddressRepository extends TransactionalCrudRepository<Address, Integer> implements AddressRepository
{

    public TransactionalAddressRepository(EntityManager entityManager)
    {
        super(entityManager, Address.class);
    }

    public TransactionalAddressRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Address.class);
    }

    public TransactionalAddressRepository()
    {
        super(Address.class);
    }

    /**
     * Returns an existing address matching the provided information, or creates a new record.
     *
     * @param street      The street of the address to getCities or create.
     * @param information The information attribute of the address to getCities or create.
     * @param city        The city of the address to getCities or create.
     * @return The resulting address entity.
     */
    @Override public Address getOrCreate(String street, String information, City city)
    {
        try {
            return this
                    .getEntityManager()
                    .createQuery("SELECT a FROM Address a WHERE a.street = :s AND a.information = :i AND city.id = :c", Address.class)
                    .setParameter("s", street)
                    .setParameter("i", information)
                    .setParameter("c", city.getId())
                    .getSingleResult();
        } catch (NoResultException e) {
            return persist(new Address(street, information, city));
        }
    }
}
