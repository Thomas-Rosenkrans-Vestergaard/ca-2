package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hobby
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Lob
    @Column(length = 255, nullable = false, unique = true)
    private String description;

    @ManyToMany
    private List<Person> persons = new ArrayList<>();

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

    public List<Person> getPersons()
    {
        return this.persons;
    }

    public void setPersons(List<Person> person)
    {
        this.persons = person;
    }
}
