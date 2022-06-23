package com.krech.botv3.domain.rest.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {

    private String word;
    private String firstLetter;


}
