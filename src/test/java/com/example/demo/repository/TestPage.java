package com.example.demo.repository;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestPage {

    @Mock
    MemberRepository memberRepository;

    @Mock
    BoardRepository boardRepository;

    @Test
    void testMemberPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = new Member();
            member.setName("test" + i);
            memberList.add(member);
        }

        Page<Member> mockPage = new PageImpl<>(memberList, pageable, memberList.size());
        Mockito.when(memberRepository.findAll(pageable)).thenReturn(mockPage);

        // when
        Page<Member> page = memberRepository.findAll(pageable);

        // then
        System.out.println("page = " + page);
    }

    @Test
    void testBoardPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        List<BoardDto> boardList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            BoardDto boardDto = new BoardDto();
            boardDto.setTitle("testTitle" + i);
            boardList.add(boardDto);
        }

        Page<BoardDto> mockPage = new PageImpl<>(boardList, pageable, boardList.size());

        Page<Board> convertedPage = mockPage.map(boardDto -> new Board(boardDto.getTitle()));

        Mockito.when(boardRepository.findAll(pageable)).thenReturn(convertedPage);

        // when
        Page<Board> page = boardRepository.findAll(pageable);

        // then
        System.out.println("page = " + page);
    }
}
