package com.example.demo.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "board")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글 번호")
    private Long bno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_sno")
    @Comment("작성자 회원 번호")
    private Member member;

    @Column(length = 500, nullable = false)
    @Comment("제목")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Comment("내용")
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    @Comment("조회수")
    private int views;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) // 부모 객체 삭제시 연관 자식 객체 함께 삭제 (게시판 - 댓글)
    private List<Reply> replies = new ArrayList<>();

    public Board(Member member, String title, String content, int views, List<Reply> replies) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.views = views;
        this.replies = replies;
    }

    public Board(String title) {
        super();
    }

    public void setBno(Long bno) {
        this.bno = bno;
    }

    public void setTitle(String updatedTitle) {
        this.title = updatedTitle;
    }

    public void setContent(String updatedContent) {
        this.content = updatedContent;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}

