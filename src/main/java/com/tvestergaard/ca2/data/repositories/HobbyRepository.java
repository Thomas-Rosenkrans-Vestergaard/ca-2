package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Hobby;

public interface HobbyRepository extends CrudRepository<Hobby, Integer>
{

    /**
     * Creates a new hobby with the provided name and description.
     *
     * @param name        The name of the hobby to create.
     * @param description The description of the hobby to create.
     * @return The new hobby entity.
     */
    Hobby create(String name, String description);
}
