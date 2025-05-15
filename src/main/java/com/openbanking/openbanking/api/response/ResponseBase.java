package com.openbanking.openbanking.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseBase {
    private Integer status;
    private String message;
}
