package com.readers.be3.vo.article.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;




public interface ArticleSearchResponseVO {
    @Schema(name = "aiSeq", description = "게시글 번호", example="1")
    public Integer getAiSeq();
    @Schema(name = "aiTitle", description = "게시글 제목", example="안녕하세요.")
    public String getAiTitle();
    @Schema(name = "aiContent", description = "게시글 내용", example="반가워요(내용).")
    public String getAiContent();
    @Schema(name = "aiRegDt" ,description = "등록일")
    public LocalDateTime getAiRegDt();
    @Schema(name = "aiMoDt", description = "수정일")
    public LocalDateTime getAiModDt();
    @Schema(name = "aiUiSeq", description = "작성한 회원 번호", example="1")
    public Integer getAiUiSeq();
    @Schema(name = "aiBiSeq", description = "책 번호", example="1")
    public Integer getAiBiSeq();
    @Schema(name = "uiNickname", description = "작성자명(닉네임)", example="testNickname")
    public String getUiNickname();
    @Schema(name = "uiNickname", description = "ISBN", example="9788979592566")
    public String getBiIsbn();
    // @Schema(name = "uiNickname", description = "좋아요/싫어요 수", example="1")
    // public String getArStatus();


}
