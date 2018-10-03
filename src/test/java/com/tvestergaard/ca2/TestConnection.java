package com.tvestergaard.ca2;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConnection
{

    public static EntityManagerFactory create()
    {
        return Persistence.createEntityManagerFactory("rest-api-test-pu");
    }
}
