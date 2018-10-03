package com.tvestergaard.ca2.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class InfoEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<Phone> phoneNumbers = new ArrayList<>();

    @ManyToOne
    private Address address;

    public InfoEntity()
    {

    }

    public InfoEntity(String email)
    {
        this.email = email;
    }

    public Integer getId()
    {
        return this.id;
    }

    public InfoEntity setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getEmail()
    {
        return this.email;
    }

    public InfoEntity setEmail(String email)
    {
        this.email = email;
        return this;
    }

    public List<Phone> getPhoneNumbers()
    {
        return this.phoneNumbers;
    }

    public InfoEntity setPhoneNumbers(List<Phone> phoneNumbers)
    {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public Address getAddress()
    {
        return this.address;
    }

    public InfoEntity setAddress(Address address)
    {
        this.address = address;
        return this;
    }
}
