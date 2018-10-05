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

    private static final EntityManagerFactory          emf        = Persistence.createEntityManagerFactory("ca2-rest-pu");
    private static final TransactionalPersonRepository repository = new TransactionalPersonRepository(emf);

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(APPLICATION_JSON)
    public Response get() throws Exception
    {
        return Response.ok()
                       .entity(gson.toJson(repository.get().stream().map(p -> new PersonDTO(p, true, true, false)).collect(Collectors.toList())))
                       .build();
    }

    @GET
    @Path("contact-info")
    @Produces(APPLICATION_JSON)
    public Response getContactInformation() throws Exception
    {
        return Response.ok()
                       .entity(gson.toJson(repository.get().stream().map(p -> new ContactDTO(p)).collect(Collectors.toList())))
                       .build();
    }

    @GET
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getWithId(@PathParam("id") int id) throws Exception
    {
        Person person = repository.get(id);
        if (person == null)
            throw new PersonNotFoundException(id);

        return Response.ok()
                       .entity(gson.toJson(new PersonDTO(person, true, true, false)))
                       .build();
    }

    @GET
    @Path("{id: [0-9]+}/contact-info")
    @Produces(APPLICATION_JSON)
    public Response getWithIdContactInformation(@PathParam("id") int id) throws Exception
    {
        Person person = repository.get(id);
        if (person == null)
            throw new PersonNotFoundException(id);

        return Response.ok()
                       .entity(gson.toJson(new ContactDTO(person)))
                       .build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response post(String received) throws Exception
    {
        PostedPerson              postedPerson         = gson.fromJson(received, PostedPerson.class);
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(postedPerson);
        if (!constraintViolations.isEmpty())
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
            for (PostPhone postPhone : postedPerson.phones)
                phoneNumbers.add(new Phone(postPhone.number, postPhone.description));
            Person person = repository.create(postedPerson.firstName, postedPerson.lastName, postedPerson.email,
                                              address,
                                              phoneNumbers);
            repository.commit();

            PersonDTO personDTO = new PersonDTO(person, true, true, false);

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
        public List<PostPhone> phones;
    }

    private static class PutPerson extends ReceivedPerson
    {

        @AssertValid
        @NotNull
        public List<PutPhone> phones;
    }

    private static class PostPhone
    {

        @NotNull
        @Length(min = 1, max = 255)
        public String number;

        @NotNull
        @Length(min = 1, max = 255)
        public String description;
    }

    private static class PutPhone extends PostPhone
    {

        @NotNull
        @Size(min = 0)
        public Integer id;
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response put(@PathParam("id") int id, String received) throws Exception
    {
        PutPerson                 putPerson            = gson.fromJson(received, PutPerson.class);
        Validator                 validator            = new Validator();
        List<ConstraintViolation> constraintViolations = validator.validate(putPerson);
        if (!constraintViolations.isEmpty())
            throw new ValidationException("Could not validate submitted person.", constraintViolations);

        TransactionalPersonRepository  repository        = new TransactionalPersonRepository(emf);
        TransactionalAddressRepository addressRepository = new TransactionalAddressRepository(repository.getEntityManager());
        TransactionalCityRepository    cityRepository    = new TransactionalCityRepository(repository.getEntityManager());
        try {
            repository.begin();
            City city = cityRepository.get(putPerson.address.city);
            if (city == null)
                throw new CityNotFoundException(putPerson.address.city);
            Address address = addressRepository.getOrCreate(putPerson.address.street,
                                                            putPerson.address.information,
                                                            city);
            List<Phone> phoneNumbers = new ArrayList<>();
            for (PutPhone putPhone : putPerson.phones)
                phoneNumbers.add(new Phone(putPhone.number, putPhone.description));
            Person person = repository.update(id, putPerson.firstName, putPerson.lastName, putPerson.email,
                                              address,
                                              phoneNumbers);
            repository.commit();

            PersonDTO personDTO = new PersonDTO(person, true, true, false);

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

    @GET
    @Path("hobby/{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response withHobbyId(@PathParam("id") int id)
    {
        return Response.ok()
                       .entity(gson.toJson(repository.withHobby(id)))
                       .build();
    }

    @GET
    @Path("hobby/{name: [a-zA-Z ]+}")
    @Produces(APPLICATION_JSON)
    public Response withHobbyName(@PathParam("name") String name)
    {
        return Response.ok()
                       .entity(gson.toJson(repository.withHobby(name)))
                       .build();
    }

    @GET
    @Path("hobby/{id: [0-9]+}/count")
    @Produces(APPLICATION_JSON)
    public Response countWithHobbyId(@PathParam("id") int id)
    {
        return Response.ok()
                       .entity(count(repository.countWithHobby(id)).toString())
                       .build();
    }

    @GET
    @Path("hobby/{name: [a-zA-Z ]+}/count")
    @Produces(APPLICATION_JSON)
    public Response countWithHobbyName(@PathParam("name") String name)
    {
        return Response.ok()
                       .entity(count(repository.countWithHobby(name)).toString())
                       .build();
    }

    @GET
    @Path("zip-code/{code}")
    @Produces(APPLICATION_JSON)
    public Response inZipCode(@PathParam("code") String code)
    {
        return Response.ok()
                       .entity(gson.toJson(repository.inZipCode(code)))
                       .build();
    }

    @GET
    @Path("city/{name: [a-zA-Z ]+}")
    @Produces(APPLICATION_JSON)
    public Response inCity(@PathParam("name") String name)
    {
        return Response.ok()
                       .entity(gson.toJson(repository.inCity("name")))
                       .build();
    }

    @GET
    @Path("city/{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response inCity(@PathParam("id") int id)
    {
        return Response.ok()
                       .entity(gson.toJson(repository.inCity(id)))
                       .build();
    }

    @DELETE
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws Exception
    {
        TransactionalPersonRepository personRepository = new TransactionalPersonRepository(emf);
        try {
            personRepository.begin();
            Person person = personRepository.delete(id);
            personRepository.commit();
            if (person == null)
                throw new PersonNotFoundException(id);

            return Response.ok(gson.toJson(new PersonDTO(person, true, true, false))).build();
        } finally {
            personRepository.close();
        }
    }

    private static JsonObject count(int count)
    {
        JsonObject o = new JsonObject();
        o.addProperty("count", count);
        return o;
    }
}
