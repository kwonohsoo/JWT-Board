package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.QReply;
import com.example.demo.entity.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ReplyQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    // 댓글 목록 조회
    public List<Reply> findRepliesByBoard(Board board) {
        QReply reply = QReply.reply;

        return queryFactory
                .selectFrom(reply)
                .where(reply.bno.eq(board))
                .fetch();
    }

    // 댓글 작성
    @Transactional
    public Long saveReply(Reply reply) {
        em.persist(reply);
        return reply.getRno();
    }

    // 댓글 수정
    @Transactional
    public void updateReply(Reply updatedReply) {
        em.merge(updatedReply);
    }

    // 댓글 삭제
    @Transactional
    public void deleteReply(Long replyId) {
        QReply reply = QReply.reply;
        queryFactory
                .delete(reply)
                .where(reply.rno.eq(replyId))
                .execute();
    }
}
