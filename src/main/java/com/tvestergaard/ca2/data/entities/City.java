package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;

@Entity
public class City
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, nullable = false)
    private String zipCode;

    @Column(length = 50, nullable = false)
    private String name;

    public City()
    {

    }

    public City(String zipCode, String name)
    {
        this.zipCode = zipCode;
        this.name = name;
    }

    public Integer getId()
    {
        return this.id;
    }

    public City setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getZipCode()
    {
        return this.zipCode;
    }

    public City setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public City setName(String city)
    {
        this.name = city;
        return this;
    }
}
