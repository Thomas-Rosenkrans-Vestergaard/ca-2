package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.City;

public class CityDTO
{

    private int    id;
    private String zipCode;
    private String name;

    public CityDTO(City city)
    {
        this.id = city.getId();
        this.zipCode = city.getZipCode();
        this.name = city.getName();
    }
}
