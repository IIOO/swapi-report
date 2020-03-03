package com.aleksander.test.report.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.net.URI;


/**
 * Film
 * <p>
 * A Star Wars film
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "url",
})
@Data
public class FilmDto {
    /**
     * The title of this film.
     * (Required)
     *
     */
    @JsonProperty("title")
    @JsonPropertyDescription("The title of this film.")
    private String title;
    /**
     * The url of this resource
     * (Required)
     *
     */
    @JsonProperty("url")
    @JsonPropertyDescription("The url of this resource")
    private URI url;
}
