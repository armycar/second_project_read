package com.readers.be3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "한줄평Request")
public class OneCommentRequest {
  @Schema(description = "유저 번호" , example = "1")
  private Long uiSeq;
  @Schema(description = "책번호" , example = "1")
  private Long biSeq;
  @Schema(description = "한줄평 내용" , example = "content")
  private String content;
  @Schema(description = "한줄평 점수" , example = "3")
  private Integer score;
}
