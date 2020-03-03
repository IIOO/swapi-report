package com.aleksander.test.report.dto.response;

import com.aleksander.test.report.dto.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PeopleResponse {
    private String next;
    private List<Person> results;

    public PeopleResponse() {
        this.results = new ArrayList<>();
    }
}
