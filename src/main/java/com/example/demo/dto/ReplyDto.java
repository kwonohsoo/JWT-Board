package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "댓글 응답 정보")
public class ReplyDto {

    @ApiModelProperty(position = 1, value = "댓글 번호", example = "1")
    private Long rno;

    @ApiModelProperty(position = 2, value = "댓글 내용", example = "댓글 내용")
    private String content;

    @ApiModelProperty(position = 3, value = "작성자 회원 번호", example = "1")
    private Long writerSno;

    public ReplyDto(Long rno, String content) {
        this.rno = rno;
        this.content = content;
    }

}
