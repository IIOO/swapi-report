package com.aleksander.test.report.domain.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryResponse {
    private String next;
    private List<Object> results;

    public QueryResponse() {
        this.results = new ArrayList<>();
    }
}
