package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "토큰 응답 정보")
public class TokenDto {

    @ApiModelProperty(position = 1, required = true, value = "AccessToken 정보", example = "Encoded AccessToken")
    private String accessToken;

    @ApiModelProperty(position = 2, required = true, value = "RefreshToken 정보", example = "Encoded RefreshToken")
    private String refreshToken;

    public TokenDto() {

    }
}
