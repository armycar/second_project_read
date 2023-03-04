package com.readers.be3.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "article_info")
public class ArticleInfoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "게시글 번호", example = "1")  
    @Column(name = "ai_seq") private Long aiSeq;
    @Schema(description = "게시글 제목", example = "안녕하세요")  
    @Column(name = "ai_title") private String aiTitle;
    @Schema(description = "게시글 내용", example = "반갑습니다")  
    @Column(name = "ai_content") private String aiContent;
    @Schema(description = "등록일", example = "")  
    @Column(name = "ai_reg_dt") private LocalDateTime aiRegDt;
    @Schema(description = "수정일", example = "null")  
    @Column(name = "ai_mod_dt") private LocalDateTime aiModDt;
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")  
    @Column(name = "ai_status") private Integer aiStatus;
    @Schema(description = "상태(1.게시판 2.리뷰 3. 이벤트)", example = "1")  
    @Column(name = "ai_purpose") private Integer aiPurpose;
    @Schema(description = "상태(1.공개 2.비공개)", example = "1")  
    @Column(name = "ai_public") private Integer aiPublic;
    @Schema(description = "책번호", example = "1")  
    @Column(name = "ai_bi_seq") private Long aiBiSeq;
    @Schema(description = "회원 번호", example = "1")  
    @Column(name = "ai_ui_seq") private Long aiUiSeq;

   
    
}
