package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Hobby;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransactionalHobbyRepository extends TransactionalCrudRepository<Hobby, Integer> implements HobbyRepository
{

    public TransactionalHobbyRepository(EntityManager entityManager)
    {
        super(entityManager, Hobby.class);
    }

    public TransactionalHobbyRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Hobby.class);
    }

    public TransactionalHobbyRepository()
    {
        super(Hobby.class);
    }

    @Override public Hobby create(String name, String description)
    {
        Hobby hobby = new Hobby(name, description);

        return persist(hobby);
    }
}
