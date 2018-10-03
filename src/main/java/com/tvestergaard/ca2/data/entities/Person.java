package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
})
public class Person extends InfoEntity
{

    @Column(length = 255, nullable = false)
    private String firstName;

    @Column(length = 255, nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "persons")
    private List<Hobby> hobbies = new ArrayList<>();

    public Person()
    {

    }

    public Person(String firstName, String lastName, String email)
    {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public Person setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public Person setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public List<Hobby> getHobbies()
    {
        return this.hobbies;
    }

    public void setHobbies(List<Hobby> hobbies)
    {
        this.hobbies = hobbies;
    }
}
