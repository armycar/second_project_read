package com.readers.be3.dto.response;

import java.time.LocalDateTime;

import com.readers.be3.entity.OneCommentEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Schema(description = "한줄평VO")
public class OneCommentResponse {
  @Schema(description = "한줄평 내용")
  private String content;
  @Schema(description = "한줄평 점수")
  private Integer score;
  @Schema(description = "한줄평 등록일")
  private LocalDateTime regDt;
  @Schema(description = "한줄평 닉네임")
  private String nickName;

  public static OneCommentResponse toResponse(OneCommentEntity entity){
    return new OneCommentResponse(entity.getOcComment(),
                                  entity.getOcScore(),
                                  entity.getOcRegDt(),
                                  entity.getUserInfoEntity().getUiNickname());
  }
}
