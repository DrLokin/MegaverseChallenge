package com.crossmint.polyantet.service.request_body;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestBody {

    final String candidateId;
    final int row, column;
}
