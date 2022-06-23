package com.krech.botv3.domain.rest.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WordResponse {
    private int id;
    private String word;
    private String firstLetter;


}
