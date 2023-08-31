package com.example.demo.repository;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.entity.QBoard;
import com.example.demo.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryRepository {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final JPAQueryFactory queryFactory;

    // 게시글 작성
    @Transactional
    public BoardDto saveBoard(BoardDto boardDto) {
        Member author = memberRepository.findById(boardDto.getWriterSno())
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .views(boardDto.getViews())
                .member(author)
                .build();

        boardRepository.save(board);

        return boardDto;
    }

    // 게시글 목록 조회 (페이징)
    public Page<Board> getAll(Pageable pageable) {

        List<Board> results = queryFactory
                .selectFrom(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    // 게시글 상세 조회
    public Board findBoardByBno(Long bno) {

        QBoard board = QBoard.board;

        return queryFactory
                .selectFrom(board)
                .leftJoin(board.replies, QReply.reply).fetchJoin()
                .where(board.bno.eq(bno))
                .fetchOne();

    }

    // 조회수 증가
    public long updateViewCount(Long bno) {
        return queryFactory
                .update(board)
                .set(board.views, board.views.add(1))
                .where(board.bno.eq(bno))
                .execute();
    }

    // 게시글 검색 (제목 또는 내용 포함)
    public List<Board> searchBoards(String keyword) {

        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null) {
            builder.or(board.title.containsIgnoreCase(keyword));
            builder.or(board.content.containsIgnoreCase(keyword));
        }

        return queryFactory
                .selectFrom(board)
                .where(builder)
                .fetch();
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long bno, BoardDto updatedBoard) {

        QBoard qBoard = board;

        queryFactory
                .update(qBoard)
                .set(qBoard.title, updatedBoard.getTitle())
                .set(qBoard.content, updatedBoard.getContent())
                .where(qBoard.bno.eq(bno))
                .execute();
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long bno) {

        QBoard qBoard = QBoard.board;
        QReply qReply = QReply.reply;

        queryFactory
                .delete(qReply)
                .where(qReply.board.bno.eq(bno))
                .execute();

        queryFactory
                .delete(qBoard)
                .where(qBoard.bno.eq(bno))
                .execute();
    }
}
