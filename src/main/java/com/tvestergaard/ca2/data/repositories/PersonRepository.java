package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.entities.Phone;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer>
{

    /**
     * Creates a new person from the provided information.
     *
     * @param firstName The first name of the person to create.
     * @param lastName  The last name of the person to create.
     * @param email     The email address of the person to create.
     * @param address   The address of the person to create.
     * @param phones    The phone numbers of the person to create.
     * @return The created person.
     */
    Person create(String firstName, String lastName, String email, Address address, List<Phone> phones);

    /**
     * Updates the person with the provided id to the provided values.
     *
     * @param id        The id of the person to update.
     * @param firstName The new first name.
     * @param lastName  The new last name.
     * @param email     The new email.
     * @param address   The address of the person.
     * @param phones    The phone numbers of the person.
     * @return The updated person.
     */
    Person update(int id, String firstName, String lastName, String email, Address address, List<Phone> phones);

    /**
     * Returns the person with the provided phone number.
     *
     * @param phoneNumber The phone number of the person to return.
     * @return The person with the provided phone number, or {@code null} when no such person exists.
     */
    Person withPhoneNumber(String phoneNumber);

    /**
     * Returns all the people with the hobby with the provided name.
     *
     * @param hobbyName The name of the hobby of the people to return.
     * @return The the people with the hobby with the provided name.
     */
    List<Person> withHobby(String hobbyName);

    /**
     * Returns all the people with the hobby with the provided id.
     *
     * @param hobbyId The id of the hobby of the people to return.
     * @return The the people with the hobby with the provided id.
     */
    List<Person> withHobby(Integer hobbyId);

    /**
     * Returns the number of people with the hobby with the provided id.
     *
     * @param hobbyId The id of the hobby to return the people of.
     * @return The number of people with the hobby with the provided id.
     */
    int countWithHobby(Integer hobbyId);

    /**
     * Returns the number of people with the hobby with the provided name.
     *
     * @param hobbyName The name of the hobby to return the people of.
     * @return The number of people with the hobby with the provided name.
     */
    int countWithHobby(String hobbyName);

    /**
     * Finds all the people in the city with the provided zip-code.
     *
     * @param zipCode The zip-code to return the inhabitants of.
     * @return The people in the city with the provided zip-code.
     */
    List<Person> inZipCode(String zipCode);

    /**
     * Finds all the people in the provided city.
     *
     * @param city The city to return the inhabitants of.
     * @return The people in the provided city.
     */
    List<Person> inCity(City city);

    /**
     * Finds all the people in the city with the provided name.
     *
     * @param name The name to return the inhabitants of.
     * @return The people in the city with the provided name.
     */
    List<Person> inCity(String name);

    /**
     * Finds all the people in the city with the provided id.
     *
     * @param id The name to return the inhabitants of.
     * @return The people in the city with the provided id.
     */
    List<Person> inCity(Integer id);
}
