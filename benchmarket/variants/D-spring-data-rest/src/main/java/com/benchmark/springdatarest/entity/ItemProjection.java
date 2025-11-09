package com.benchmark.springdatarest.entity;

import org.springframework.data.rest.core.config.Projection;
import java.math.BigDecimal;

@Projection(name = "itemSummary", types = { Item.class })
public interface ItemProjection {
    Long getId();
    String getSku();
    String getName();
    BigDecimal getPrice();
    Integer getStock();
    String getCategoryCode();
    String getCategoryName();
}