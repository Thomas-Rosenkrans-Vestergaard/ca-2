package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvestergaard.ca2.data.entities.Hobby;
import com.tvestergaard.ca2.data.repositories.TransactionalHobbyRepository;
import com.tvestergaard.ca2.rest.dto.HobbyDTO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("hobbies")
public class HobbyResource
{

    private static final EntityManagerFactory emf  = Persistence.createEntityManagerFactory("ca2-rest-pu");
    private static final     Gson                 gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(APPLICATION_JSON)
    public Response getHobbies() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.get())))
                        .build());
    }

    @FunctionalInterface
    private interface ExceptionFunction
    {
        public Response call(TransactionalHobbyRepository repository) throws Exception;
    }

    private Response repository(ExceptionFunction f) throws Exception
    {
        TransactionalHobbyRepository repository = new TransactionalHobbyRepository(emf);
        try {
            return f.call(repository);
        } finally {
            repository.close();
        }
    }

    private List<HobbyDTO> toDTOs(List<Hobby> entities)
    {
        List<HobbyDTO> result = new ArrayList<>();
        for (Hobby hobby : entities)
            result.add(new HobbyDTO(hobby));

        return result;
    }
}
