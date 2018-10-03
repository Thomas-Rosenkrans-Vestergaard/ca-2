package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.TestConnection;
import com.tvestergaard.ca2.data.entities.City;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.TestCase.*;

public class TransactionalCityRepositoryTest
{

    private static final EntityManagerFactory        emf = TestConnection.create();
    private              TransactionalCityRepository instance;

    @Before
    public void setUp()
    {
        instance = new TransactionalCityRepository(emf);
        instance.begin();
    }

    @After
    public void tearDown()
    {
        instance.rollback();
    }

    @Test
    public void getZipCodes()
    {
        List<String> result = instance.getZipCodes();

        assertEquals(9, result.size());
        assertEquals("zip1", result.get(0));
        assertEquals("zip9", result.get(8));
    }

    @Test
    public void withZipCode()
    {
        City city = instance.withZipCode("zip5");

        assertEquals(5, (int) city.getId());
        assertEquals("city5", city.getName());
        assertEquals("zip5", city.getZipCode());
    }

    @Test
    public void withZipCodeReturnsNull()
    {
        assertNull(instance.withZipCode(""));
    }

    @Test
    public void get()
    {
        List<City> result = instance.get();

        assertEquals(9, result.size());
        assertEquals("zip1", result.get(0).getZipCode());
        assertEquals("zip9", result.get(8).getZipCode());
    }

    @Test
    public void getId()
    {
        City cityA = instance.get(1);

        assertEquals(1, (int) cityA.getId());
        assertEquals("city1", cityA.getName());
        assertEquals("zip1", cityA.getZipCode());
    }

    @Test
    public void getIdReturnsNull()
    {
        assertNull(instance.get(-1));
    }

    @Test
    public void persist()
    {
        String expectedName = "New City";
        String expectedZip  = "____";

        City city = new City(expectedZip, expectedName);
        instance.persist(city);
        assertNotNull(city.getId());
        assertEquals(expectedZip, city.getZipCode());
        assertEquals(expectedName, city.getName());
    }

    @Test
    public void update()
    {
        City created = instance.persist(new City("A", "B"));

        assertEquals("A", created.getZipCode());
        assertEquals("B", created.getName());

        created.setZipCode("C");
        created.setName("D");
        instance.update(created);

        assertEquals("C", created.getZipCode());
        assertEquals("D", created.getName());
    }

    @Test
    public void delete()
    {
        City created = instance.persist(new City("E", "F"));
        int  id      = created.getId();
        assertEquals(created, instance.get(id));
        assertEquals(created, instance.delete(id));
        assertNull(instance.get(id));
    }

    @Test
    public void deleteReturnsNull()
    {
        assertNull(instance.delete(-1));
    }
}