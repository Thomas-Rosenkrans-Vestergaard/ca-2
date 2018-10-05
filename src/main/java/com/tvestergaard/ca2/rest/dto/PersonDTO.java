package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonDTO
{
    public int            id;
    public String         firstName;
    public String         lastName;
    public String         email;
    public List<PhoneDTO> phones;
    public AddressDTO     address;

    public PersonDTO(Person person, boolean showPhoneNumbers, boolean showAddress, boolean showHobbies)
    {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        if (showPhoneNumbers)
            this.phones = person.getPhoneNumbers()
                                .stream()
                                .map(phoneNumber -> new PhoneDTO(phoneNumber))
                                .collect(Collectors.toList());
        if(showAddress && person.getAddress() != null)
            this.address = new AddressDTO(person.getAddress(), true);
    }
}
