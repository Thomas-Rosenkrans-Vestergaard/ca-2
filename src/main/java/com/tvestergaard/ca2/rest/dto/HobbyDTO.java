package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Hobby;

public class HobbyDTO
{

    private int    id;
    private String name;
    private String description;

    public HobbyDTO(Hobby hobby)
    {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.description = hobby.getDescription();
    }
}
