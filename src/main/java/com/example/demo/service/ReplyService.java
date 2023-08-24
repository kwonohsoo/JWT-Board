package com.example.demo.service;

import com.example.demo.dto.ReplyDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Reply;
import com.example.demo.repository.ReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyQueryRepository replyQueryRepository;


    // 댓글 작성
    @Transactional
    public ReplyDto saveReply(ReplyDto replyDto) {
        return replyQueryRepository.saveReply(replyDto);
    }

    // 댓글 조회
    public List<Reply> findRepliesByBoard(Board board){
        return replyQueryRepository.findRepliesByBoard(board);
    }

    // 댓글 수정
    public void updateReply(Long rno, ReplyDto replyDto) {
        replyQueryRepository.updateReply(rno, replyDto);
    }


}
