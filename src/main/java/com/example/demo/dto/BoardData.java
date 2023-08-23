package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class BoardData {
    private long bno;
    private String title;
    private String content;
    private int views;
    private String writerName;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
