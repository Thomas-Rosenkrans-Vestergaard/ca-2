package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;

@Entity
public class Phone
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private InfoEntity owner;

    public Phone()
    {

    }

    public Phone(String number, String description)
    {
        this.number = number;
        this.description = description;
    }

    public Integer getId()
    {
        return this.id;
    }

    public Phone setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getNumber()
    {
        return this.number;
    }

    public Phone setNumber(String number)
    {
        this.number = number;
        return this;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Phone setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public InfoEntity getOwner()
    {
        return this.owner;
    }

    public Phone setOwner(InfoEntity owner)
    {
        this.owner = owner;
        return this;
    }
}
