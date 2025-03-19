package com.vet.app.dtos.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoImageRequest {
    private Long userId;
    private String imageBase64;
}
