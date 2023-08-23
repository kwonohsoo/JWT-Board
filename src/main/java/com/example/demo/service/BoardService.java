package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardQueryRepository boardQueryRepository;

    // 게시글 저장
    @Transactional
    public BoardDto saveBoard(BoardDto boardDto) {
        return boardQueryRepository.saveBoard(boardDto);
    }

    // 게시글 목록 조회 (페이징)
    public BoardResponseDto getAll(Pageable pageable) {

        Page<Board> result = boardQueryRepository.getAll(pageable);

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .totalPage(result.getTotalPages())
                .totalCount(result.getTotalElements())
                .pageSize(result.getPageable().getPageNumber() + 1)
                .pageSize(result.getPageable().getPageSize())
                .boardDataList(new ArrayList<>())
                .build();

        boardResponseDto.mySetBoardDataList(result.getContent());

        return boardResponseDto;
    }

    // 게시글 상세 조회
    public BoardDto findBoardByBno(Long bno) {

        Board board = boardQueryRepository.findBoardByBno(bno);

        if (board != null) {
            BoardDto boardDto = new BoardDto();
            boardDto.setBno(board.getBno());
            boardDto.setTitle(board.getTitle());
            boardDto.setContent(board.getContent());
            return boardDto;
        } else {
            return null;
        }
    }

    // 게시글 검색
    public List<BoardDto> searchBoard(String keyword) {
        List<Board> boards = boardQueryRepository.searchBoards(keyword);
        List<BoardDto> list = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = new BoardDto(board.getBno(), board.getTitle(), board.getContent());
            list.add(boardDto);
        }
        return list;
    }

    // 게시글 수정
    public void updateBoard(Long bno, BoardDto updatedBoard) {
        boardQueryRepository.updateBoard(bno, updatedBoard);
    }

    //게시글 삭제
    @Transactional
    public void deleteBoard(Long bno) {
        boardQueryRepository.deleteBoard(bno);
    }
}
