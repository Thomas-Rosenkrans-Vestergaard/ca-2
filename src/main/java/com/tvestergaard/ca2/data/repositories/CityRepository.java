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
}
