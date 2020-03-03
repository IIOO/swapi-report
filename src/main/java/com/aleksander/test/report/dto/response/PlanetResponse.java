package com.aleksander.test.report.dto.response;

import com.aleksander.test.report.dto.Planet;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlanetResponse {
    private String next;
    private List<Planet> results;

    public PlanetResponse() {
        this.results = new ArrayList<>();
    }
}
