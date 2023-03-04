package com.readers.be3.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.readers.be3.entity.OneCommentEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;


@Schema(description = "한줄평리스트 반환값")
@AllArgsConstructor
public class OneCommentListDTO {
  @Schema(description = "한줄평번호")
  @JsonProperty
  private Long onecommentSeq;
  @Schema(description = "한줄평 내용")
  @JsonProperty
  private String content;
  @Schema(description = "한줄평 점수")
  @JsonProperty
  private Integer score;
  @Schema(description = "한줄평 등록일")
  @JsonProperty
  private LocalDateTime regDt;
  @Schema(description = "유저 닉네임")
  @JsonProperty
  private String nickName;
  @Schema(description = "유저 경험치")
  @JsonProperty
  private Integer totalPoint;

  public static OneCommentListDTO toDto(OneCommentEntity entity){
    return new OneCommentListDTO(entity.getOcSeq(), 
                                  entity.getOcComment(), 
                                  entity.getOcScore(),
                                   entity.getOcRegDt(), 
                                   entity.getUserInfoEntity().getUiNickname(),
                                   entity.getUserInfoEntity().getUiPoint());
  }
}
