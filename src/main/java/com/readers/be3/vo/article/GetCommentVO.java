package com.readers.be3.vo.article;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;


public interface GetCommentVO {

    @Schema(name = "content", description = "댓글 내용", example = "댓글 내용입니다.")
    public String getAcContent();
    @Schema(description = "댓글 등록일")
    public LocalDateTime getAcRegDt();
    @Schema(description = "댓글 수정일")
    public LocalDateTime getAcModDt();
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")
    public Integer getAcStatus();
    @Schema(description = "게시글 번호", example = "1")
    public Long getAcAiSeq();
    @Schema(description = "댓글 작성자 회원번호", example = "1")
    public Long getAcUiSeq();
    
}
