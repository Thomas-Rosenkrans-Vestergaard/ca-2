package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Person;

import java.util.List;
import java.util.stream.Collectors;

public class ContactDTO
{

    public int            id;
    public String         firstName;
    public String         lastName;
    public String         email;
    public List<PhoneDTO> phones;

    public ContactDTO(Person person)
    {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.phones = person.getPhoneNumbers().stream().map(p -> new PhoneDTO(p)).collect(Collectors.toList());
    }
}
