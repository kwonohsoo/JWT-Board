package com.example.demo.dto;

import com.example.demo.entity.Board;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ApiModel(value = "게시판 응답 정보")
public class BoardResponseDto {

    @ApiModelProperty(position = 1, value = "전체 페이지 수")
    private int totalPage;

    @ApiModelProperty(position = 2, value = "전체 데이터 수")
    private long totalCount;

    @ApiModelProperty(position = 3, value = "페이지 번호")
    private int pageNumber;

    @ApiModelProperty(position = 4, value = "페이지 사이즈")
    private int pageSize;

    @ApiModelProperty(position = 5, value = "board리스트")
    List<BoardData> boardDataList;

    public void mySetBoardDataList(List<Board> boardList) {
        boardList.forEach(board -> this.boardDataList.add(
                BoardData.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .views(board.getViews())
                        .writerName(board.getMember().getName())
                        .createDt(board.getCreateDate())
                        .updateDt(board.getModifiedDate())
                        .build()));
    }
}
