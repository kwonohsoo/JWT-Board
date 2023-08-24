package com.example.demo.service;

import com.example.demo.dto.ReplyDto;
import com.example.demo.repository.ReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private  final ReplyQueryRepository replyQueryRepository;

    @Transactional
    public ReplyDto saveReply(ReplyDto replyDto) {
        return replyQueryRepository.saveReply(replyDto);
    }
}
