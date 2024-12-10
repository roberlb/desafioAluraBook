package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBook(
        String title,
        List<DataAuthor> authors,
        List<String> languages
) {
}
