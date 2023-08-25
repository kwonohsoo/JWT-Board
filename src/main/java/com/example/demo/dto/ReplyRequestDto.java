package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReplyRequestDto {

    @ApiModelProperty(position = 1, value = "댓글 내용", example = "댓글 내용")
    private String content;

    @ApiModelProperty(position = 2, value = "작성자 회원 번호", example = "1")
    private Long writerSno;

    @ApiModelProperty(position = 3, value = "게시글 번호", example = "1")
    private Long bno;

}
