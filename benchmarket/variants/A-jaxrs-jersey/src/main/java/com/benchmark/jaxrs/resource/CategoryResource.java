package com.benchmark.jaxrs.resource;

import com.benchmark.jaxrs.dto.CategoryDTO;
import com.benchmark.jaxrs.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class CategoryResource {

    private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private CategoryService categoryService;

    @GET
    public Response getCategories(@QueryParam("page") @DefaultValue("0") int page,
                                  @QueryParam("size") @DefaultValue("20") int size) {
        logger.info("GET /categories?page={}&size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.findAll(pageable);

        return Response.ok(categories.getContent()).build();
    }

    @GET
    @Path("/{id}")
    public Response getCategory(@PathParam("id") Long id) {
        logger.info("GET /categories/{}", id);
        CategoryDTO category = categoryService.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));

        return Response.ok(category).build();
    }

    @POST
    public Response createCategory(@Valid CategoryDTO categoryDTO) {
        logger.info("POST /categories");
        CategoryDTO saved = categoryService.save(categoryDTO);

        return Response.created(URI.create("/categories/" + saved.getId()))
                      .entity(saved)
                      .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, @Valid CategoryDTO categoryDTO) {
        logger.info("PUT /categories/{}", id);
        if (!categoryService.existsById(id)) {
            throw new NotFoundException("Category not found");
        }

        categoryDTO.setId(id);
        CategoryDTO updated = categoryService.save(categoryDTO);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        logger.info("DELETE /categories/{}", id);
        if (!categoryService.existsById(id)) {
            throw new NotFoundException("Category not found");
        }

        categoryService.deleteById(id);

        return Response.noContent().build();
    }
}
