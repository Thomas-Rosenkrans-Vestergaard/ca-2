package com.tvestergaard.ca2.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Hobby
{

    @Id
    private int id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String description;

    @ManyToOne
    private Person person;

    public Hobby()
    {

    }

    public Hobby(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public int getId()
    {
        return this.id;
    }

    public Hobby setId(int id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public Hobby setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Hobby setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Person getPerson()
    {
        return this.person;
    }

    public Hobby setPerson(Person person)
    {
        this.person = person;
        return this;
    }
}
