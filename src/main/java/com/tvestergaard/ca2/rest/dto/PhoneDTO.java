package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Company;
import com.tvestergaard.ca2.data.entities.InfoEntity;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.entities.Phone;

public class PhoneDTO
{

    private int        id;
    private String     number;
    private String     description;
    private PersonDTO  person;
    private CompanyDTO company;

    public PhoneDTO(Phone phone)
    {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();

        InfoEntity owner = phone.getOwner();
        if (owner instanceof Person)
            this.person = new PersonDTO((Person) owner, false, true, false);
        if (owner instanceof Company)
            this.company = new CompanyDTO((Company) owner, true, false);
    }
}
