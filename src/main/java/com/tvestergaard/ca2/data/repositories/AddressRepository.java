package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;

public interface AddressRepository extends CrudRepository<Address, Integer>
{

    /**
     * Returns an existing address matching the provided information, or creates a new record.
     *
     * @param street      The street of the address to get or create.
     * @param information The information attribute of the address to get or create.
     * @param city        The city of the address to get or create.
     * @return The resulting address entity.
     */
    Address getOrCreate(String street, String information, City city);
}
