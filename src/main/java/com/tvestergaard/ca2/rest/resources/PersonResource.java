package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tvestergaard.ca2.data.entities.Person;
import com.tvestergaard.ca2.data.repositories.TransactionalPersonRepository;
import com.tvestergaard.ca2.rest.dto.PersonDTO;

import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("persons")
public class PersonResource
{

    private static final TransactionalPersonRepository repository = new TransactionalPersonRepository(
            Persistence.createEntityManagerFactory("ca2-rest-pu")
    );

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(APPLICATION_JSON)
    public Response get() throws Exception
    {
        return Response.ok()
                       .entity(gson.toJson(repository.get()))
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
                       .entity(gson.toJson(person))
                       .build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response post(String received) throws Exception
    {
        ReceivedPerson receivedPerson = gson.fromJson(received, ReceivedPerson.class);
        Person person = repository.commit(() -> {
            return repository.create(receivedPerson.firstName, receivedPerson.lastName, receivedPerson.email);
        });
        PersonDTO personDTO = new PersonDTO(person, true, true);

        return Response.status(201)
                       .entity(gson.toJson(personDTO))
                       .build();
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
        ReceivedPerson receivedPerson = gson.fromJson(received, ReceivedPerson.class);
        Person person = repository.commit(() -> {
            return repository.update(id, receivedPerson.firstName, receivedPerson.lastName, receivedPerson.email);
        });
        PersonDTO personDTO = new PersonDTO(person, true, true);

        return Response.status(201)
                       .entity(gson.toJson(personDTO))
                       .build();
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
