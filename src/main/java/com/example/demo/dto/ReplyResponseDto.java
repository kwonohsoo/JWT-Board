package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyResponseDto {

    @ApiModelProperty(position = 1, value = "댓글 번호", example = "1")
    private Long rno;

    public ReplyResponseDto(Long rno) {
        this.rno = rno;
    }
}
