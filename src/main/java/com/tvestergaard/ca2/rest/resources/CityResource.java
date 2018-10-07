package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvestergaard.ca2.data.entities.City;
import com.tvestergaard.ca2.data.repositories.TransactionalCityRepository;
import com.tvestergaard.ca2.rest.dto.CityDTO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("cities")
public class CityResource
{

    private static final EntityManagerFactory        emf        = Persistence.createEntityManagerFactory("ca2-rest-pu");
    private static final TransactionalCityRepository repository = new TransactionalCityRepository(emf);
    private static final Gson                        gson       = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Returns a compleete list of the cities in the system.
     *
     * @return The response containing the cities in the system.
     * @throws Exception
     */
    @GET
    @Produces(APPLICATION_JSON)
    public Response getCities() throws Exception
    {
        return Response.ok()
                       .entity(gson.toJson(repository.get().stream().map(c -> new CityDTO(c)).collect(Collectors.toList())))
                       .build();
    }

    /**
     * Returns the city with the provided id.
     *
     * @param id The id of the city to return.
     * @return The city with the provided id, or {@link CityNotFoundException} when no such city exists.
     * @throws Exception
     */
    @GET
    @Path("{id: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getCityWithId(@PathParam("id") int id) throws Exception
    {
        City city = repository.get(id);
        if (city == null)
            throw new CityNotFoundException(id);

        return Response.ok()
                       .entity(gson.toJson(new CityDTO(city)))
                       .build();
    }

    /**
     * Returns the complete list of the zip-codes in the system.
     *
     * @return The complete list of the zip-codes in the system.
     */
    @GET
    @Path("zip-codes")
    @Produces(APPLICATION_JSON)
    public Response getZipCodes()
    {
        return Response.ok()
                       .entity(gson.toJson(repository.getZipCodes()))
                       .build();
    }
}
