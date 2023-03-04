
package com.readers.be3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "수정 Request dto")
public class OneCommentUpdateRequest {
    @Schema(description = "유저 번호")
    private Long uiSeq; 
    @Schema(description = "한줄평 번호")
    private Long onecommentSeq;
    @Schema(description = "수정한줄평 내용")
    private String content;
    @Schema(description = "수정할 평점")
    private Integer score;
}
