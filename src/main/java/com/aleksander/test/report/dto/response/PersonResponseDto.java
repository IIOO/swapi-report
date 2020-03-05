package com.aleksander.test.report.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * People
 * <p>
 * A person within the Star Wars universe
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "homeworld",
        "name",
        "url",
        "films"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDto {

    /**
     * The url of the planet resource that this person was born on.
     * (Required)
     *
     */
    @JsonProperty("homeworld")
    @JsonPropertyDescription("The url of the planet resource that this person was born on.")
    private String homeworld;
    /**
     * The name of this person.
     * (Required)
     *
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The name of this person.")
    private String name;
    /**
     * The url of this resource
     * (Required)
     *
     */
    @JsonProperty("url")
    @JsonPropertyDescription("The url of this resource")
    private URI url;
    /**
     * An array of urls of film resources that this person has been in.
     * (Required)
     *
     */
    @JsonProperty("films")
    @JsonPropertyDescription("An array of urls of film resources that this person has been in.")
    private List<URI> films = new ArrayList<>();
}
