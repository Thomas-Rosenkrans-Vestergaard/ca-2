package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows for retrieval and other operations on city entities.
 */
public class TransactionalPersonRepository extends TransactionalCrudRepository<Person, Integer> implements PersonRepository
{

    public TransactionalPersonRepository(EntityManager entityManager)
    {
        super(entityManager, Person.class);
    }

    public TransactionalPersonRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Person.class);
    }

    public TransactionalPersonRepository()
    {
        super(Person.class);
    }

    @Override public Person create(String firstName, String lastName, String email, Address address, List<Phone> phones)
    {
        Person person = new Person(firstName, lastName, email);
        person.setAddress(address);
        person.setPhoneNumbers(phones);
        for (Phone phone : phones)
            phone.setOwner(person);
        return persist(person);
    }

    @Override public Person delete(Integer id)
    {
        EntityManager entityManager = this.getEntityManager();
        Person        find          = entityManager.find(Person.class, id);
        if (find == null)
            return null;

        find.setAddress(null);
        find.setHobbies(new ArrayList<>());
        entityManager.remove(find);
        return find;
    }

    @Override public List<Person> withName(String firstName, String lastName)
    {
        StringBuilder builder = new StringBuilder("SELECT p FROM Person p WHERE p.id != null ");
        if (firstName != null)
            builder.append("AND p.firstName = :firstName ");
        if (lastName != null)
            builder.append("AND p.lastName = :lastName");

        TypedQuery<Person> query = getEntityManager().createQuery(builder.toString(), Person.class);
        if (firstName != null)
            query.setParameter("firstName", firstName);
        if (lastName != null)
            query.setParameter("lastName", lastName);

        return query.getResultList();
    }

    @Override
    public Person update(int id, String firstName, String lastName, String email, Address address, List<Phone> phones)
    {
        Person person = get(id);
        if (person == null)
            return null;

        EntityManager entityManager = getEntityManager();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setAddress(address);
        for(Phone phone : person.getPhoneNumbers())
            phone.setOwner(null);
        person.getPhoneNumbers().clear();
        for(Phone phone : phones) {
            phone.setOwner(person);
            person.getPhoneNumbers().add(phone);
        }

        entityManager.merge(person);

        return person;
    }

    @Override public List<Person> withPhoneNumber(String phoneNumber)
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.createQuery(
                "SELECT c FROM Person c INNER JOIN Phone p ON c.id = p.owner.id WHERE p.number = :phone",
                Person.class)
                            .setParameter("phone", phoneNumber)
                            .getResultList();
    }

    @Override public List<Person> withHobby(String hobbyName)
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.createQuery("SELECT elements(h.persons) FROM Hobby h WHERE h.name = :hobbyName",
                Person.class)
                            .setParameter("hobbyName", hobbyName)
                            .getResultList();
    }

    @Override public List<Person> withHobby(Integer hobbyId)
    {
        EntityManager entityManager = getEntityManager();

        return entityManager.createQuery("SELECT elements(h.persons) FROM Hobby h WHERE h.id = :hobbyId", Person.class)
                            .setParameter("hobbyId", hobbyId)
                            .getResultList();
    }

    @Override public int countWithHobby(Integer hobbyId)
    {
        return getEntityManager()
                .createQuery("SELECT size(h.persons) FROM Hobby h WHERE h.id = :id", Integer.class)
                .setParameter("id", hobbyId)
                .getSingleResult();
    }

    @Override public int countWithHobby(String hobbyName)
    {
        return getEntityManager()
                .createQuery("SELECT size(h.persons) FROM Hobby h WHERE h.name = :name", Integer.class)
                .setParameter("name", hobbyName)
                .getSingleResult();
    }

    @Override public List<Person> inCity(City city)
    {
        return inCity(city.getId());
    }

    @Override public List<Person> inCity(Integer id)
    {
        return getEntityManager().createQuery("SELECT p FROM Person p WHERE p.address.city.id = :cityId", Person.class)
                                 .setParameter("cityId", id)
                                 .getResultList();
    }

    @Override public List<Person> inZipCode(String zipCode)
    {
        return getEntityManager().createQuery("SELECT p FROM Person p WHERE p.address.city.zipCode = :zipCode", Person.class)
                                 .setParameter("zipCode", zipCode)
                                 .getResultList();
    }

    @Override public List<Person> inCity(String cityName)
    {
        return getEntityManager().createQuery("SELECT p FROM Person p WHERE p.address.city.name = :cityName", Person.class)
                                 .setParameter("cityName", cityName)
                                 .getResultList();
    }

    @Override public List<Person> byAddress(String street, Integer city)
    {
        StringBuilder builder = new StringBuilder("SELECT p FROM Person p WHERE p.id != null ");
        if (street != null)
            builder.append("AND p.address.street = :street ");
        if (city != null)
            builder.append("AND p.address.city.id = :city");

        TypedQuery<Person> query = getEntityManager().createQuery(builder.toString(), Person.class);
        if (street != null)
            query.setParameter("street", street);
        if (city != null)
            query.setParameter("city", city);

        return query.getResultList();
    }
}
