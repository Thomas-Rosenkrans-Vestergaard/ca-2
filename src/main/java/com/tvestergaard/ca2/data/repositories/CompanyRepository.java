package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Company;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Integer>
{

    /**
     * Returns the company with the provided phone number.
     *
     * @param phoneNumber The phone number of the company to return.
     * @return The company with the provided phone number, or {@code null} when no such company exists.
     */
    Company withPhoneNumber(String phoneNumber);

    /**
     * Returns the company with the provided cvr.
     *
     * @param cvr The cvr of the company to return.
     * @return The company with the provided cvr, or {@code null} when no such company exists.
     */
    Company withCVR(String cvr);

    /**
     * Returns the companies with more than the provided number of employees.
     *
     * @param numberOfEmployees The minimum number of employees returned companies must have.
     * @return The companies with more than the providede number of employees.
     */
    List<Company> withMoreThan(int numberOfEmployees);
}
