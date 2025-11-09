package com.benchmark.resteasy.resource;

import com.benchmark.resteasy.dto.ItemDTO;
import com.benchmark.resteasy.service.ItemService;
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

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ItemResource {

    private static final Logger logger = LoggerFactory.getLogger(ItemResource.class);

    @Autowired
    private ItemService itemService;

    @GET
    public Response getItems(@QueryParam("page") @DefaultValue("0") int page,
                             @QueryParam("size") @DefaultValue("20") int size,
                             @QueryParam("categoryId") Long categoryId) {
        logger.info("GET /items?page={}&size={}&categoryId={}", page, size, categoryId);

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDTO> items;

        if (categoryId != null) {
            items = itemService.findByCategoryId(categoryId, pageable);
        } else {
            items = itemService.findAll(pageable);
        }

        return Response.ok(items.getContent()).build();
    }

    @GET
    @Path("/{id}")
    public Response getItem(@PathParam("id") Long id) {
        logger.info("GET /items/{}", id);
        ItemDTO item = itemService.findById(id)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        return Response.ok(item).build();
    }

    @POST
    public Response createItem(@Valid ItemDTO itemDTO) {
        logger.info("POST /items");
        ItemDTO saved = itemService.save(itemDTO);

        return Response.created(URI.create("/items/" + saved.getId()))
                      .entity(saved)
                      .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, @Valid ItemDTO itemDTO) {
        logger.info("PUT /items/{}", id);
        if (!itemService.existsById(id)) {
            throw new NotFoundException("Item not found");
        }

        itemDTO.setId(id);
        ItemDTO updated = itemService.save(itemDTO);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        logger.info("DELETE /items/{}", id);
        if (!itemService.existsById(id)) {
            throw new NotFoundException("Item not found");
        }

        itemService.deleteById(id);

        return Response.noContent().build();
    }
}
