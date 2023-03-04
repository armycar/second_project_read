package com.readers.be3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OneCommentDeleteRequest {
    @Schema(description = "유저 번호" , example = "30")
    private Long uiSeq;
    @Schema(description = "한줄평번호", example = "20")
    private Long oneCommentSeq;
  
}
