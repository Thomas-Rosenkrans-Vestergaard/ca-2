package com.tvestergaard.ca2.data.repositories;

import java.util.List;

public interface CrudRepository<E, ID>
{

    /**
     * Returns all the entities in the repository.
     *
     * @return All the entities in the repository.
     */
    public List<E> get();

    /**
     * Returns the entity with the provided id.
     *
     * @param id The id of the entity to return.
     * @return The entity with the provided id, or {@code null} when no such entity exsits.
     */
    public E get(ID id);

    /**
     * Persists the provided entity.
     *
     * @param entity The entity to persist.
     * @return The updated entity.
     */
    E persist(E entity);

    /**
     * Forces the entity to update.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     */
    E update(E entity);

    /**
     * Deletes the entity with the provided id.
     *
     * @param id The id of the entity to delete.
     * @return The deleted entity, or {@code null} when no entity was deleted.
     */
    E delete(ID id);
}
