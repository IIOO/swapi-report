package com.aleksander.test.report.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.net.URI;


/**
 * Planet
 * <p>
 * A planet.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "name",
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PlanetDto {
    /**
     * The hypermedia URL of this resource.
     * (Required)
     *
     */
    @JsonProperty("url")
    @JsonPropertyDescription("The hypermedia URL of this resource.")
    private URI url;
    /**
     * The name of this planet.
     * (Required)
     *
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The name of this planet.")
    private String name;
}
