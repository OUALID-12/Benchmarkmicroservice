
package com.benchmark.springdatarest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FetchMode {
    @Value("${USE_JOIN_FETCH:false}")
    private boolean useJoinFetch;

    public boolean useJoinFetch() {
        return useJoinFetch;
    }
}
