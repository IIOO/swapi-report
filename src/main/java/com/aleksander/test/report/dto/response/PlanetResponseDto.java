package com.aleksander.test.report.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class PlanetResponseDto {
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
