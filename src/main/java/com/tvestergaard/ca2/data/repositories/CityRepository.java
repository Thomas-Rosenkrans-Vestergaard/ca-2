package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.City;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer>
{

    /**
     * Returns a complete list of the zip codes in the system.
     *
     * @return The list of the zip codes in the system.
     */
    List<String> getZipCodes();

    /**
     * Returns the city with the provided zip code.
     *
     * @param zipCode The zip code of the city to find.
     * @return The city with the provided zip code, or {@code null} when no such city exists.
     */
    City withZipCode(String zipCode);
}
