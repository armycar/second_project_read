package com.readers.be3.vo.article;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {
    @Schema(description = "게시글 번호", example = "1")  
    private Long aiSeq;
    @Schema(description = "게시글 제목", example = "제목입니다.")  
    private String aiTitle;
    @Schema(description = "게시글 내용", example = "내용입니다.")  
    private String content;
    @Schema(description = "등록일" )
    private LocalDateTime regDt;
    @Schema(description = "수정일")  
    private LocalDateTime aiModDt;
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")  
    private Integer aiStatus;
    @Schema(description = "상태(1.게시판 2.리뷰 3. 이벤트)", example = "1")  
    private Integer aiPurpose;
    @Schema(description = "상태(1.공개 2.비공개)", example = "1")  
    private Integer aiPublic;
    @Schema(description = "책번호", example = "1")  
    private Long biSeq;
    @Schema(description = "회원 번호", example = "1")  
    private Long uiSeq;
    @Schema(description = "이미지 목록")  
    List<GetImgInfoVO> showImgInfo;
    @Schema(description = "댓글목록")  
    List<GetCommentVO> showComment;
}
