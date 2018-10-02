package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Company;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;


/**
 * Allows for retrieval and other operations on city entities.
 */
public class TransactionalCompanyRepository extends TransactionalCrudRepository<Company, Integer> implements CompanyRepository
{

    public TransactionalCompanyRepository(EntityManager entityManager)
    {
        super(entityManager, Company.class);
    }

    public TransactionalCompanyRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Company.class);
    }

    public TransactionalCompanyRepository()
    {
        super(Company.class);
    }

    @Override public Company withPhoneNumber(String phoneNumber)
    {
        EntityManager entityManager = getEntityManager();

        try {
            return entityManager.createQuery("SELECT p.owner FROM Phone p WHERE p.number = :phoneNumber", Company.class)
                                .setParameter("phoneNumber", phoneNumber)
                                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override public Company withCVR(String cvr)
    {
        EntityManager entityManager = getEntityManager();

        try {
            return entityManager.createQuery("SELECT c FROM Company c WHERE c.cvr = :cvr", Company.class)
                                .setParameter("cvr", cvr)
                                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override public List<Company> withMoreThan(int numberOfEmployees)
    {
        return getEntityManager()
                .createQuery("SELECT c FROM Company c WHERE c.numberOfEmployees > :numberOfEmployees", Company.class)
                .setParameter("numberOfEmployees", numberOfEmployees)
                .getResultList();
    }
}
