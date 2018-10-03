package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.repositories.TransactionalPersonRepository;
import com.tvestergaard.ca2.rest.dto.ContactDTO;
import com.tvestergaard.ca2.rest.dto.PersonDTO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
        ReceivedPerson                receivedPerson = gson.fromJson(received, ReceivedPerson.class);
        TransactionalPersonRepository repository     = new TransactionalPersonRepository(emf);
        try {
            repository.begin();
            Person person = repository.create(receivedPerson.firstName, receivedPerson.lastName, receivedPerson.email);
            repository.commit();

            PersonDTO personDTO = new PersonDTO(person, true, true, false);

            return Response.status(201)
                           .entity(gson.toJson(personDTO))
                           .build();
        } finally {
            repository.close();
        }
    }

    private class ReceivedPerson
    {
        public String firstName;
        public String lastName;
        public String email;
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response put(@PathParam("id") int id, String received) throws Exception
    {
        ReceivedPerson                receivedPerson = gson.fromJson(received, ReceivedPerson.class);
        TransactionalPersonRepository repository     = new TransactionalPersonRepository(emf);
        try {
            repository.begin();
            Person person = repository.update(id, receivedPerson.firstName, receivedPerson.lastName, receivedPerson.email);
            if (person == null)
                throw new PersonNotFoundException(id);
            repository.commit();
            PersonDTO personDTO = new PersonDTO(person, true, true, false);

            return Response.status(201)
                           .entity(gson.toJson(personDTO))
                           .build();
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

    private static JsonObject count(int count)
    {
        JsonObject o = new JsonObject();
        o.addProperty("count", count);
        return o;
    }
}
