package com.example.demo.controller;

import com.example.demo.dto.ReplyRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Reply;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
@Api(tags = "댓글 관리")
public class ReplyController {

    private final ReplyService replyService;
    private final BoardRepository boardRepository;

    // 댓글 작성
    @PostMapping("/create")
    @ApiOperation(value = "댓글 작성 API", notes = "새로운 댓글 작성")
    public ResponseEntity<?> saveReply(@RequestBody ReplyRequestDto replyRequestDto) {
        try {
            Long savedReply = replyService.saveReply(replyRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성에 실패하였습니다.");
        }
    }

    // 댓글 조회
    @GetMapping("/read/{bno}")
    @ApiOperation(value = "댓글 목록 조회 API", notes = "게시글에 해당하는 댓글 목록 조회")
    public ResponseEntity<List<Reply>> readReplies(@PathVariable Long bno) {
        Board board = boardRepository.findById(bno).orElse(null);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        List<Reply> replies = replyService.findRepliesByBoard(board);
        log.info(replies.toString());
        return ResponseEntity.ok(replies);
    }

    // 댓글 수정
    @PutMapping("/update/{rno}")
    @ApiOperation(value = "댓글 수정 API", notes = "댓글 수정")
    public ResponseEntity<String> updateReply(@PathVariable Long rno, @RequestBody ReplyRequestDto replyRequestDto) {
        try {
            replyService.updateReply(rno, replyRequestDto);
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다. ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정 중 오류가 발생하였습니다.");
        }
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{rno}")
    @ApiOperation(value = "댓글 삭제 API", notes = "댓글 삭제")
    public ResponseEntity<String> deleteReply(@PathVariable Long rno) {
        try {
            replyService.deleteReply(rno);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생하였습니다.");
        }
    }
}
