package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Phone;

public class PhoneDTO
{

    private int    id;
    private String number;
    private String description;

    public PhoneDTO(Phone phone)
    {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();
    }
}
