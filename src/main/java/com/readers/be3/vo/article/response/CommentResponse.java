package com.readers.be3.vo.article.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    @Schema(description = "댓글번호", example = "1")
    private Long acSeq;
    @Schema(description = "댓글 내용", example = "1")
    private String acContent;
    @Schema(description = "등록일")
    private LocalDateTime acRegDt;
    @Schema(description = "수정일")
    private LocalDateTime acModDt;
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")
    private Integer acStatus;
    @Schema(description = "게시글 번호", example = "1")
    private Long acAiSeq;
    @Schema(description = "회원 번호(작성자)", example = "1")
    private Long acUiSeq;
}
