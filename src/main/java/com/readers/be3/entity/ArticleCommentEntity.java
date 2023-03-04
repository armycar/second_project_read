package com.readers.be3.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "article_comment")
public class ArticleCommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(description = "댓글번호", example = "1")
    @Column(name = "ac_seq") private Long acSeq;
    @Schema(description = "댓글 내용", example = "1")
    @Column(name = "ac_content") private String acContent;
    @Schema(description = "등록일")
    @Column(name = "ac_reg_dt") private LocalDateTime acRegDt;
    @Schema(description = "수정일")
    @Column(name = "ac_mod_dt") private LocalDateTime acModDt;
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")
    @Column(name = "ac_status") private Integer acStatus;
    @Schema(description = "게시글 번호", example = "1")
    @Column(name = "ac_ai_seq") private Long acAiSeq;
    @Schema(description = "회원 번호(작성자)", example = "1")
    @Column(name = "ac_ui_seq") private Long acUiSeq;
}
