package com.readers.be3.dto.request;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.readers.be3.entity.OneCommentEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Schema(description = "한줄평리스트 반환값")
@Data
@AllArgsConstructor
public class OneCommentViewsDTO {
  @Schema(description = "한줄평번호")
  @JsonProperty
  private Long ocSeq;
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
  private String userNickName;
  @Schema(description = "유저 경험치")
  @JsonProperty
  private Integer userPoint;
  @Schema(description = "한줄평 조회수")
  @JsonProperty
  private Integer commentViews;

  public static OneCommentViewsDTO toDto(OneCommentEntity entity){
    return new OneCommentViewsDTO(entity.getOcSeq(), 
                                  entity.getOcComment(), 
                                  entity.getOcScore(),
                                   entity.getOcRegDt(), 
                                   entity.getUserInfoEntity().getUiNickname(),
                                   entity.getUserInfoEntity().getUiPoint(),
                                   entity.getOcViews());
  }
}
