package com.tvestergaard.ca2;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConnection
{

    private static EntityManagerFactory singleton = null;

    public static EntityManagerFactory create()
    {
        if (singleton == null)
            singleton = Persistence.createEntityManagerFactory("rest-api-test-pu");

        return singleton;
    }
}
