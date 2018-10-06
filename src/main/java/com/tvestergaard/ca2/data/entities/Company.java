package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Company extends InfoEntity
{

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 255, nullable = false, unique = true)
    private String cvr;

    @Column(nullable = false)
    private Integer numberOfEmployees;

    @Column(nullable = false)
    private Integer marketValue;

    public Company()
    {

    }

    public Company(String name, String description, String cvr, Integer numberOfEmployees, Integer marketValue,
                   String email)
    {
        super(email);
        this.name = name;
        this.description = description;
        this.cvr = cvr;
        this.numberOfEmployees = numberOfEmployees;
        this.marketValue = marketValue;
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

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return Objects.equals(getId(), company.getId()) &&
                Objects.equals(getName(), company.getName()) &&
                Objects.equals(getDescription(), company.getDescription()) &&
                Objects.equals(getCvr(), company.getCvr()) &&
                Objects.equals(getNumberOfEmployees(), company.getNumberOfEmployees()) &&
                Objects.equals(getMarketValue(), company.getMarketValue());
    }

    @Override public int hashCode()
    {
        return Objects.hash(getId(), getName(), getDescription(), getCvr(), getNumberOfEmployees(), getMarketValue());
    }

    @Override public String toString()
    {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cvr='" + cvr + '\'' +
                ", numberOfEmployees=" + numberOfEmployees +
                ", marketValue=" + marketValue +
                '}';
    }
}
