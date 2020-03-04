package com.aleksander.test.report.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QueryResponse {
    private String next;
    private List<Object> results;

    public QueryResponse() {
        this.results = new ArrayList<>();
    }
}
