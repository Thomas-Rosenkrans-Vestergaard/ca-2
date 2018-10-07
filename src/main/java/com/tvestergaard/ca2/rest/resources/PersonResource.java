package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tvestergaard.ca2.data.entities.Address;
import com.tvestergaard.ca2.data.entities.City;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.entities.Phone;
import com.tvestergaard.ca2.data.repositories.TransactionalAddressRepository;
import com.tvestergaard.ca2.data.repositories.TransactionalCityRepository;
import com.tvestergaard.ca2.data.repositories.TransactionalPersonRepository;
import com.tvestergaard.ca2.rest.dto.ContactDTO;
import com.tvestergaard.ca2.rest.dto.PersonDTO;
import com.tvestergaard.ca2.rest.exceptions.ValidationException;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("persons")
public class PersonResource
{

    private static final EntityManagerFactory emf  = Persistence.createEntityManagerFactory("ca2-rest-pu");
    private static final Gson                 gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(APPLICATION_JSON)
    public Response getPersons() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(repository.get().stream().map(p -> new PersonDTO(p)).collect(Collectors.toList())))
                        .build());
    }

    @GET
    @Path("paginated/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsPaginated(@PathParam("pageSize") int pageSize, @PathParam("pageNumber") int pageNumber) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.get(pageSize, pageNumber))))
                        .build());
    }

    @GET
    @Path("street/{street: .*}/city/{city: [0-9]*}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsByAddress(@PathParam("street") String street, @PathParam("city") int city) throws Exception
    {
        String  s = street.equals("_") ? null : street;
        Integer c = city == 0 ? null : city;

        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.byAddress(s, c))))
                        .build());
    }

    @GET
    @Path("count")
    @Produces(APPLICATION_JSON)
    public Response countPersons() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(count(repository.count())))
                        .build());
    }

    @GET
    @Path("contact-info")
    @Produces(APPLICATION_JSON)
    public Response getContactInformations() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.get())))
                        .build());
    }

    @GET
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonWithId(@PathParam("id") int id) throws Exception
    {
        return repository(repository -> {
            Person person = repository.get(id);
            if (person == null)
                throw new PersonNotFoundException(id);

            return Response.ok()
                           .entity(gson.toJson(new PersonDTO(person)))
                           .build();
        });
    }

    @GET
    @Path("first/{first: .*}/last/{last: .*}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsWithName(@PathParam("first") String first, @PathParam("last") String last) throws Exception
    {
        String firstName = first.equals("_") ? null : first;
        String lastName  = last.equals("_") ? null : last;

        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.withName(firstName, lastName))))
                        .build());
    }

    @GET
    @Path("{id: [0-9]+}/contact-info")
    @Produces(APPLICATION_JSON)
    public Response getContactInformationWithId(@PathParam("id") int id) throws Exception
    {
        return repository(repository -> {
            Person person = repository.get(id);
            if (person == null)
                throw new PersonNotFoundException(id);

            return Response.ok()
                           .entity(gson.toJson(new ContactDTO(person)))
                           .build();
        });
    }

    @GET
    @Path("hobby/{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsWithHobbyId(@PathParam("id") int id) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.withHobby(id))))
                        .build());
    }

    @GET
    @Path("hobby/{name: [a-zA-Z ]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsWithHobbyName(@PathParam("name") String name) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.withHobby(name))))
                        .build());
    }

    @GET
    @Path("phone/{phoneNumber: .*}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsWithPhoneNumber(@PathParam("phoneNumber") String phoneNumber) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.withPhoneNumber(phoneNumber))))
                        .build());
    }

    @GET
    @Path("hobby/{id: [0-9]+}/getPersonsCount")
    @Produces(APPLICATION_JSON)
    public Response countPersonsWithHobbyId(@PathParam("id") int id) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(count(repository.countWithHobby(id)).toString())
                        .build());
    }

    @GET
    @Path("hobby/{name: [a-zA-Z ]+}/getPersonsCount")
    @Produces(APPLICATION_JSON)
    public Response countPersonsWithHobbyName(@PathParam("name") String name) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(count(repository.countWithHobby(name)).toString())
                        .build());
    }

    @GET
    @Path("zip-code/{code}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsInZipCode(@PathParam("code") String code) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.inZipCode(code))))
                        .build());
    }

    @GET
    @Path("city/{name: [a-zA-Z ]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsInCityName(@PathParam("name") String name) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.inCity(name))))
                        .build());
    }

    @GET
    @Path("city/{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getPersonsInCityId(@PathParam("id") int id) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.inCity(id))))
                        .build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createPerson(String received) throws Exception
    {
        PostedPerson              postedPerson         = gson.fromJson(received, PostedPerson.class);
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(postedPerson);
        if (!constraintViolations.equals("_"))
            throw new ValidationException("Could not validate submitted person.", constraintViolations);

        TransactionalPersonRepository  repository        = new TransactionalPersonRepository(emf);
        TransactionalAddressRepository addressRepository = new TransactionalAddressRepository(repository.getEntityManager());
        TransactionalCityRepository    cityRepository    = new TransactionalCityRepository(repository.getEntityManager());
        try {
            repository.begin();
            City city = cityRepository.get(postedPerson.address.city);
            if (city == null)
                throw new CityNotFoundException(postedPerson.address.city);
            Address address = addressRepository.getOrCreate(postedPerson.address.street,
                    postedPerson.address.information,
                    city);
            List<Phone> phoneNumbers = new ArrayList<>();
            for (PostedPhone postPhone : postedPerson.phones)
                phoneNumbers.add(new Phone(postPhone.number, postPhone.description));
            Person person = repository.create(postedPerson.firstName, postedPerson.lastName, postedPerson.email,
                    address,
                    phoneNumbers);
            repository.commit();

            PersonDTO personDTO = new PersonDTO(person);

            return Response.status(201)
                           .entity(gson.toJson(personDTO))
                           .build();
        } catch (PersistenceException e) {
            repository.rollback();
            throw e;
        } finally {
            repository.close();
        }
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") int id, String received) throws Exception
    {
        PostedPerson              postedPerson         = gson.fromJson(received, PostedPerson.class);
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(postedPerson);
        if (!constraintViolations.equals("_"))
            throw new ValidationException("Could not validate submitted person.", constraintViolations);

        TransactionalPersonRepository  repository        = new TransactionalPersonRepository(emf);
        TransactionalAddressRepository addressRepository = new TransactionalAddressRepository(repository.getEntityManager());
        TransactionalCityRepository    cityRepository    = new TransactionalCityRepository(repository.getEntityManager());
        try {
            repository.begin();
            City city = cityRepository.get(postedPerson.address.city);
            if (city == null)
                throw new CityNotFoundException(postedPerson.address.city);
            Address address = addressRepository.getOrCreate(postedPerson.address.street,
                    postedPerson.address.information,
                    city);
            List<Phone> phoneNumbers = new ArrayList<>();
            for (PostedPhone postedPhone : postedPerson.phones)
                phoneNumbers.add(new Phone(postedPhone.number, postedPhone.description));
            Person person = repository.update(id, postedPerson.firstName, postedPerson.lastName, postedPerson.email,
                    address,
                    phoneNumbers);
            repository.commit();

            PersonDTO personDTO = new PersonDTO(person);

            return Response.status(200)
                           .entity(gson.toJson(personDTO))
                           .build();
        } catch (PersistenceException e) {
            repository.rollback();
            throw e;
        } finally {
            repository.close();
        }
    }

    @DELETE
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") int id) throws Exception
    {
        TransactionalPersonRepository personRepository = new TransactionalPersonRepository(emf);
        try {
            personRepository.begin();
            Person    person    = personRepository.delete(id);
            PersonDTO personDTO = new PersonDTO(person);
            personRepository.commit();
            if (person == null)
                throw new PersonNotFoundException(id);

            return Response.ok(gson.toJson(personDTO)).build();
        } finally {
            personRepository.close();
        }
    }

    private static JsonObject count(long count)
    {
        JsonObject o = new JsonObject();
        o.addProperty("count", count);
        return o;
    }

    @FunctionalInterface
    private interface ExceptionFunction
    {
        public Response call(TransactionalPersonRepository repository) throws Exception;
    }

    private Response repository(ExceptionFunction f) throws Exception
    {
        TransactionalPersonRepository repository = new TransactionalPersonRepository(emf);
        try {
            return f.call(repository);
        } finally {
            repository.close();
        }
    }

    private List<PersonDTO> toDTOs(List<Person> entities)
    {
        List<PersonDTO> result = new ArrayList<>();
        for (Person person : entities)
            result.add(new PersonDTO(person));

        return result;
    }

    private static class ReceivedPerson
    {

        @NotNull
        @Length(min = 1, max = 255)
        public String firstName;

        @NotNull
        @Length(min = 1, max = 255)
        public String lastName;

        @NotNull
        @Length(min = 1, max = 255)
        @Email
        public String email;

        @AssertValid
        @NotNull
        public ReceivedAddress address;
    }

    private static class ReceivedAddress
    {

        @NotNull
        @Length(min = 1, max = 255)
        public String street;

        @NotNull
        @Length(min = 1, max = 255)
        public String information;

        @Size(min = 1)
        public int city;
    }

    private static class PostedPerson extends ReceivedPerson
    {

        @AssertValid
        @NotNull
        public List<PostedPhone> phones;
    }

    private static class PostedPhone
    {

        @NotNull
        @Length(min = 1, max = 255)
        public String number;

        @NotNull
        @Length(min = 1, max = 255)
        public String description;
    }
}
