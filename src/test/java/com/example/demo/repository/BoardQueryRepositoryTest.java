package com.example.demo.repository;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
@DisplayName("Board Repository TEST")
@Slf4j
public class BoardQueryRepositoryTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardQueryRepository boardQueryRepository;

    private List<Board> boards;

    @BeforeEach
    void setUp() {
        boards = new ArrayList<>();
        boards.add(Board.builder().bno(1L).title("제목1").content("내용1").build()); // 테스트용 게시글 추가
        boards.add(Board.builder().bno(2L).title("제목2").content("내용2").build());
        boards.add(Board.builder().bno(3L).title("제목3").content("내용3").build());
        boards.add(Board.builder().bno(4L).title("제목4").content("내용4").build());
        boards.add(Board.builder().bno(5L).title("제목5").content("내용5").build());
        boards.add(Board.builder().bno(6L).title("제목6").content("내용6").build());
        boards.add(Board.builder().bno(7L).title("제목7").content("내용7").build());
        boards.add(Board.builder().bno(8L).title("제목8").content("내용8").build());
        boards.add(Board.builder().bno(9L).title("제목9").content("내용9").build());
        boards.add(Board.builder().bno(10L).title("제목10").content("내용10").build());
        boards.add(Board.builder().bno(11L).title("제목11").content("내용11").build());

    }

    @Test
    @DisplayName("게시글 등록")
    void 게시글_등록() {
        // given
        String title = "제목1";
        String content = "내용1";
        Long writerSno = 1L;

        when(memberRepository.findById(writerSno)).thenReturn(Optional.of(new Member()));

        BoardDto boardDto = BoardDto.builder()
                .title(title)
                .content(content)
                .views(1)
                .writerSno(writerSno)
                .bno(1L)
                .build();

        // when
        boardQueryRepository.saveBoard(boardDto);

        // then
        verify(boardRepository, times(1)).save(any(Board.class));

        log.info(boardDto.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시판 목록 조회 페이징")
    void 게시판_목록_조회_페이징() {
        // given
        Pageable pageable = Pageable.ofSize(5); // 페이징 정보를 생성 size: 5
        Page<Board> boardPage = new PageImpl<>(boards, pageable, boards.size()); // Page 객체 생성
        when(boardRepository.findAll(pageable)).thenReturn(boardPage); // Mock 객체를 이용하여 boardQueryRepository.getAll() 메소드가 모의 데이터 반환하도록 설정

        // when
        Page<Board> result = boardRepository.findAll(pageable);

        // then
        assertEquals(boards.size(), result.getContent().size()); // 기대한 게시글 수와 실제 결과의 게시글 수 비교
        verify(boardRepository, times(1)).findAll(pageable);

        log.info(result.toString());
        log.info("--------------------------------------------------");
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void 게시글_상세_조회() {
        // given
        Long bno = 1L; // 게시글 번호
        Board board = boards.get(0);
        when(boardRepository.findById(bno)).thenReturn(Optional.of(board));

        // when
        Optional<Board> resultOptional = boardRepository.findById(bno);

        // then
        assertTrue(resultOptional.isPresent());
        Board result = resultOptional.get();
        assertEquals(board.getBno(), result.getBno());
        assertEquals(board.getTitle(), result.getTitle());
        assertEquals(board.getContent(), result.getContent());
        assertEquals(board.getViews(), result.getViews());


        log.info("게시글 상세 정보:");
        log.info("BNO: " + result.getBno());
        log.info("Title: " + result.getTitle());
        log.info("Content: " + result.getContent());
        log.info("Views: " + result.getViews());
        log.info("--------------------------------------------------");
    }


    @Test
    @DisplayName("조회수 증가")
    void 조회수_증가() {
        // given


        // when

        // then
    }

    @Test
    @DisplayName("게시글 검색(제목or내용)")
    void 게시글_검색() {
        // given


        // when

        // then
    }

    @Test
    @DisplayName("게시글 수정")
    void 게시글_수정() {
        // given


        // when

        // then
    }

    @Test
    @DisplayName("게시글 삭제")
    void 게시글_삭제() {
        // given


        // when

        // then
    }
}