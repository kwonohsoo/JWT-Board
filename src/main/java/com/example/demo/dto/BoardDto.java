package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "게시판 응답 정보")
public class BoardDto {

    @ApiModelProperty(position = 1, value = "게시글 번호", example = "1")
    private Long bno;

    @Getter
    @ApiModelProperty(position = 2, value = "작성자 회원 번호", example = "1")
    private Long writerSno;

    @NotBlank(message = "제목을 입력해주세요.")
    @ApiModelProperty(position = 3, required = true, value = "제목", example = "게시글 제목")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @ApiModelProperty(position = 4, required = true, value = "내용", example = "게시글 내용")
    private String content;

    @ApiModelProperty(position = 5, value = "조회수", example = "0")
    private int views;

    public BoardDto(Long bno, String title, String content) {
        this.bno = bno;
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
