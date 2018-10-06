package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDTO
{

    public  int            id;
    public  String         name;
    public  String         description;
    public  String         cvr;
    public  int            numberOfEmployees;
    public  int            marketValue;
    private String         email;
    private List<PhoneDTO> phoneNumbers = new ArrayList<>();
    private Address        address;

    public CompanyDTO(Company company)
    {
        this(company, true, true);
    }

    public CompanyDTO(Company company, boolean showAddress, boolean showPhoneNumbers)
    {
        this.id = company.getId();
        this.name = company.getName();
        this.description = company.getDescription();
        this.cvr = company.getCvr();
        this.numberOfEmployees = company.getNumberOfEmployees();
        this.marketValue = company.getMarketValue();
        this.email = company.getEmail();
        if (showAddress)
            this.address = company.getAddress();
        if (showPhoneNumbers)
            this.phoneNumbers = company.getPhoneNumbers()
                                       .stream()
                                       .map(p -> new PhoneDTO(p))
                                       .collect(Collectors.toList());
        ;
    }
}
