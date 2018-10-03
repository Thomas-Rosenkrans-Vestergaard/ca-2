package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.TestConnection;
import com.tvestergaard.ca2.data.entities.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.TestCase.*;

public class TransactionalCompanyRepositoryTest
{

    private static final EntityManagerFactory           emf = TestConnection.create();
    private              TransactionalCompanyRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalCompanyRepository(emf);
        instance.begin();
    }

    @After
    public void tearDown() throws Exception
    {
        instance.rollback();
        instance.getEntityManager().close();
    }

    @Test
    public void withPhoneNumber()
    {
        Company company = instance.withPhoneNumber("number8");

        assertNotNull(company);
        assertEquals("name3", company.getName());
    }

    @Test
    public void withPhoneNumberReturnsNull()
    {
        assertNull(instance.withPhoneNumber(""));
    }

    @Test
    public void withCVR()
    {
        Company company = instance.withCVR("cvr2");

        assertNotNull(company);
        assertEquals("name2", company.getName());
    }

    @Test
    public void withCVRReturnsNull()
    {
        assertNull(instance.withCVR(""));
    }

    @Test
    public void withMoreThan()
    {
        List<Company> result = instance.withMoreThan(3);

        assertEquals(2, result.size());
        assertEquals("name4", result.get(0).getName());
        assertEquals("name5", result.get(1).getName());
    }

    @Test
    public void get()
    {
        List<Company> result = instance.get();

        assertEquals(5, result.size());
        assertEquals("name1", result.get(0).getName());
        assertEquals("name5", result.get(4).getName());
    }

    @Test
    public void getById()
    {
        Company company1 = instance.get(6);

        assertNotNull(company1);
        assertEquals("name1", company1.getName());
    }

    @Test
    public void getByIdReturnsNull()
    {
        assertNull(instance.get(-1));
    }

    @Test
    public void persist()
    {
        Company company   = new Company("a", "b", "c", 1, 2, "email@email.com");
        Company persisted = instance.persist(company);

        assertNotNull(persisted);
        assertEquals(company, persisted);
    }

    @Test
    public void update()
    {
        Company persisted = instance.persist(new Company("a", "b", "c", 1, 2, "email@email.com"));

        persisted.setName("e");
        persisted.setCvr("f");
        persisted.setDescription("g");
        persisted.setNumberOfEmployees(3);
        persisted.setMarketValue(4);

        Company updated = instance.update(persisted);
        assertEquals(persisted, updated);
        assertEquals(updated, instance.get(updated.getId()));
    }

    @Test
    public void delete()
    {
        Company persisted = instance.persist(new Company("a", "b", "c", 1, 2, "email@email.com"));
        int     id        = persisted.getId();

        assertNotNull(persisted);
        assertNotNull(instance.get(id));
        assertEquals(persisted, instance.delete(id));
        assertNull(instance.get(id));
    }

    @Test
    public void deleteReturnsNull()
    {
        assertNull(instance.delete(-1));
    }
}