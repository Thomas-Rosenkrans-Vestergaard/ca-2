package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;

@Entity
public class Company extends InfoEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 255, nullable = false)
    private String cvr;

    @Column(nullable = false)
    private Integer numberOfEmployees;

    @Column(nullable = false)
    private Integer marketValue;

    public Company()
    {

    }

    public Company(String name, String description, String cvr, Integer numberOfEmployees, Integer marketValue)
    {
        this.name = name;
        this.description = description;
        this.cvr = cvr;
        this.numberOfEmployees = numberOfEmployees;
        this.marketValue = marketValue;
    }

    @Override public Integer getId()
    {
        return this.id;
    }

    public Company setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public Company setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Company setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public String getCvr()
    {
        return this.cvr;
    }

    public Company setCvr(String cvr)
    {
        this.cvr = cvr;
        return this;
    }

    public Integer getNumberOfEmployees()
    {
        return this.numberOfEmployees;
    }

    public Company setNumberOfEmployees(Integer numberOfEmployees)
    {
        this.numberOfEmployees = numberOfEmployees;
        return this;
    }

    public Integer getMarketValue()
    {
        return this.marketValue;
    }

    public Company setMarketValue(Integer marketValue)
    {
        this.marketValue = marketValue;
        return this;
    }
}