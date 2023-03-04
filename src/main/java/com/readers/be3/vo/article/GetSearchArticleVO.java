package com.readers.be3.vo.article;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 게시글 목록 조회
public class GetSearchArticleVO {
    @Schema(description = "게시글 번호")
    public Long aiSeq;
    @Schema(description = "게시글 제목")
    public String aiTitle;
    @Schema(description = "게시글 등록일")
    public LocalDateTime aiRegDt;
    @Schema(description = "게시글 수정일")
    public LocalDateTime aiModDt;
    @Schema(description = "상태(1.정상 2.삭제)")
    public Integer aiStatus;
    @Schema(description = "상태(1.게시판 2.리뷰 3. 이벤트)")
    public Integer aiPurpose;
    @Schema(description = "상태(1.공개 2.비공개)")
    public Integer aiPublic;
    @Schema(description = "회원 번호(게시글 작성자)")
    public Integer aiUiSeq;
    @Schema(description = "책 번호")
    public Integer aiBiSeq;
    @Schema(description = "닉네임(작성자)")
    public String uiNickname;
}
