package com.example.demo.controller;

import com.example.demo.dto.ReplyDto;
import com.example.demo.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
@Api(tags = "댓글 관리")
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 작성
    @PostMapping("/create")
    @ApiOperation(value = "댓글 작성 API", notes = "새로운 댓글 작성")
    public ResponseEntity<?> saveReply(@RequestBody ReplyDto replyDto) {
        try {
            ReplyDto savedReply = replyService.saveReply(replyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성에 실패하였습니다.");
        }
    }


}
