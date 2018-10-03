package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.TestConnection;
import com.tvestergaard.ca2.data.entities.Hobby;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.TestCase.*;

public class TransactionalHobbyRepositoryTest
{


    private static final EntityManagerFactory         emf = TestConnection.create();
    private              TransactionalHobbyRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalHobbyRepository(emf);
        instance.begin();
    }

    @After
    public void tearDown() throws Exception
    {
        instance.rollback();
        instance.getEntityManager().close();
    }

    @Test
    public void create()
    {
        Hobby hobby = instance.create("A", "B");

        assertNotNull(hobby.getId());
        assertEquals("A", hobby.getName());
        assertEquals("B", hobby.getDescription());
        assertEquals(0, hobby.getPersons().size());
    }

    @Test
    public void get()
    {
        List<Hobby> hobbies = instance.get();

        assertEquals(5, hobbies.size());
        assertEquals("name1", hobbies.get(0).getName());
        assertEquals("name5", hobbies.get(4).getName());
    }

    @Test
    public void count()
    {
        assertEquals(5, instance.count());
    }

    @Test
    public void getById()
    {
        Hobby hobby = instance.get(3);

        assertEquals(3, hobby.getId());
        assertEquals("name3", hobby.getName());
        assertEquals("description3", hobby.getDescription());
    }

    @Test
    public void getByIdReturnNull()
    {
        assertNull(instance.get(-1));
    }

    @Test
    public void persist()
    {
        Hobby persisted = instance.persist(new Hobby("A", "B"));
        int   id        = persisted.getId();

        assertNotNull(id);
        assertEquals(persisted, instance.get(id));
    }

    @Test
    public void update()
    {
        Hobby persisted = instance.persist(new Hobby("A", "B"));

        assertNotNull(persisted.getId());
        assertEquals("A", persisted.getName());

        persisted.setName("C");
        instance.update(persisted);

        assertEquals("C", instance.get(persisted.getId()).getName());
    }

    @Test
    public void delete()
    {
        Hobby persisted = instance.persist(new Hobby("A", "B"));
        int   id        = persisted.getId();

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