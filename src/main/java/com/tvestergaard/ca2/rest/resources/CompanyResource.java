package com.tvestergaard.ca2.rest.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tvestergaard.ca2.data.entities.Company;
import com.tvestergaard.ca2.data.repositories.TransactionalCompanyRepository;
import com.tvestergaard.ca2.rest.dto.CompanyDTO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("companies")
public class CompanyResource
{

    private static final EntityManagerFactory emf  = Persistence.createEntityManagerFactory("ca2-rest-pu");
    private static final Gson                 gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(APPLICATION_JSON)
    public Response get() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.get())))
                        .build());
    }

    @GET
    @Path("paginated/{pageSize: [0-9]+}/{pageNumber: [0-9]+}")
    @Produces(APPLICATION_JSON)
    public Response getPagination(@PathParam("pageSize") int pageSize, @PathParam("pageNumber") int pageNumber) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.get(pageSize, pageNumber))))
                        .build());
    }

    @GET
    @Path("getPersonsCount")
    @Produces(APPLICATION_JSON)
    public Response count() throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(count(repository.count())))
                        .build());
    }

    @GET
    @Path("size/{minMarketValue: [0-9]*}/{maxMarketValue: [0-9]*}/{minEmployees: [0-9]*}/{maxEmployees: [0-9]*}")
    @Produces(APPLICATION_JSON)
    public Response bySize(
            @PathParam("minMarketValue") String minMarketValue,
            @PathParam("maxMarketValue") String maxMarketValue,
            @PathParam("minEmployees") String minEmployees,
            @PathParam("maxEmployees") String maxEmployees) throws Exception
    {
        return repository(repository ->
                Response.ok()
                        .entity(gson.toJson(toDTOs(repository.bySize(
                                minMarketValue.isEmpty() ? null : Integer.parseInt(minMarketValue),
                                maxMarketValue.isEmpty() ? null : Integer.parseInt(maxMarketValue),
                                minEmployees.isEmpty() ? null : Integer.parseInt(minEmployees),
                                maxEmployees.isEmpty() ? null : Integer.parseInt(maxEmployees)))))
                        .build());
    }

    private static JsonObject count(long count)
    {
        JsonObject o = new JsonObject();
        o.addProperty("getPersonsCount", count);
        return o;
    }

    @FunctionalInterface
    private interface ExceptionFunction
    {
        public Response call(TransactionalCompanyRepository repository) throws Exception;
    }

    private Response repository(ExceptionFunction f) throws Exception
    {
        TransactionalCompanyRepository repository = new TransactionalCompanyRepository(emf);
        try {
            return f.call(repository);
        } finally {
            repository.close();
        }
    }

    private List<CompanyDTO> toDTOs(List<Company> entities)
    {
        List<CompanyDTO> result = new ArrayList<>();
        for (Company company : entities)
            result.add(new CompanyDTO(company));

        return result;
    }
}
