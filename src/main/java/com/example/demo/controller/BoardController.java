package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Api(tags = "게시판 관리")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/create")
    @ApiOperation(value = "게시글 작성 API", notes = "새로운 게시글 작성")
    public ResponseEntity<?> createBoard(@RequestBody BoardDto boardDto) {
        try {
            BoardDto createdBoard = boardService.saveBoard(boardDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("게시글 작성 실패: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("게시글 작성 실패: 작성자를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    // 게시판 조회 (페이징)
    @GetMapping("/page")
    @ApiOperation(value = "게시판 조회 API", notes = "게시판 목록 조회 (페이징)")
    public BoardResponseDto getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("pageable.getOffset: {}, pageable.getPageSize: {}", pageable.getOffset(), pageable.getPageSize());

        return boardService.getAll(pageable);
    }

    // 게시글 상세 조회
    @GetMapping("/{bno}")
    @ApiOperation(value = "게시글 상세 조회 API", notes = "게시글 상세 조회")
    public ResponseEntity<BoardDto> findBoardByBno(@PathVariable Long bno) {
        try {
            BoardDto boardDto = boardService.findBoardByBno(bno);
            if (boardDto != null) {
                return ResponseEntity.ok(boardDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 게시판 검색 기능
    @GetMapping("/search")
    @ApiOperation(value = "게시판 검색 API", notes = "게시판 키워드 검색 기능")
    public List<BoardDto> searchBoard(@RequestParam String keyword) {
        return boardService.searchBoard(keyword);
    }

    // 게시글 수정
    @PutMapping("/update/{bno}")
    @ApiOperation(value = "게시글 수정 API", notes = "게시글 수정")
    public ResponseEntity<String> updateBoard(@PathVariable Long bno, @RequestBody BoardDto updatedBoard) {
        try {
            boardService.updateBoard(bno, updatedBoard);
            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 수정 중 오류가 발생하였습니다.");
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{bno}")
    @ApiOperation(value = "게시글 삭제 API", notes = "게시글 삭제")
    public ResponseEntity<String> deleteBoard(@PathVariable Long bno) {
        try {
            boardService.deleteBoard(bno);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 중 오류가 발생하였습니다.");
        }
    }
}
