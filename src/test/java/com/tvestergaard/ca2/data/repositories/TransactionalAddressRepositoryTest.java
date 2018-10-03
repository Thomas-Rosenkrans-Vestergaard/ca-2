package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.TestConnection;
import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TransactionalAddressRepositoryTest
{

    private static final EntityManagerFactory           emf = TestConnection.create();
    private              TransactionalAddressRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalAddressRepository(emf);
        instance.begin();
    }

    @After
    public void tearDown() throws Exception
    {
        instance.rollback();
        instance.getEntityManager().close();
    }

    @Test
    public void getOrCreate()
    {
        long startCount = instance.count();

        CityRepository cityRepository = new TransactionalCityRepository(instance.getEntityManager());
        City           city           = cityRepository.get(1);

        Address existing = instance.getOrCreate("street1", "information1", city);
        assertEquals(startCount, instance.count());

        Address created = instance.getOrCreate("s", "i", city);
        assertEquals("s", created.getStreet());
        assertEquals("i", created.getInformation());
        assertEquals(startCount + 1, instance.count());
    }

    @Test
    public void get()
    {
        List<Address> results = instance.get();

        assertEquals(5, results.size());
        assertEquals("street1", results.get(0).getStreet());
        assertEquals("street5", results.get(4).getStreet());
    }

    @Test
    public void getById()
    {
        Address address = instance.get(5);

        assertEquals(5, (int) address.getId());
        assertEquals("street5", address.getStreet());
        assertEquals("information5", address.getInformation());
        assertEquals(5, (int) address.getCity().getId());
    }

    @Test
    public void count()
    {
        assertEquals(5, instance.count());
    }

    @Test
    public void persist()
    {
    }

    @Test
    public void update()
    {
    }

    @Test
    public void delete()
    {
    }
}