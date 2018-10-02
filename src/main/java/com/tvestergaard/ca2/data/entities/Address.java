package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;

@Entity
public class Address
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String street;

    @Column(length = 255, nullable = false)
    private String information;

    @ManyToOne
    private City city;

    public Address()
    {

    }

    public Address(String street, String information)
    {
        this.street = street;
        this.information = information;
    }

    public Integer getId()
    {
        return this.id;
    }

    public Address setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getStreet()
    {
        return this.street;
    }

    public Address setStreet(String street)
    {
        this.street = street;
        return this;
    }

    public String getInformation()
    {
        return this.information;
    }

    public Address setInformation(String information)
    {
        this.information = information;
        return this;
    }

    public City getCity()
    {
        return this.city;
    }

    public Address setCity(City city)
    {
        this.city = city;
        return this;
    }
}
