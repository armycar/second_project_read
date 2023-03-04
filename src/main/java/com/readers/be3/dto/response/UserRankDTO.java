package com.readers.be3.dto.response;

import com.readers.be3.entity.UserRankView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "유저 랭크 정보")
@AllArgsConstructor
@Data
public class UserRankDTO {
  @Schema(description = "랭크")
    private Long rank;
  @Schema(description = "유저네임")
    private String userName;
  @Schema(description = "전체페이지")
    private Integer totalPage;
  @Schema(description = "전체포인트")
    private Integer totalPoint;
  @Schema(description = "전체책갯수")
    private Integer totalBook;

    public static UserRankDTO toResponse(UserRankView userRankView){
      return new UserRankDTO(userRankView.getRankSeq(),
      userRankView.getUiNickName(),
      userRankView.getUiTotalPage(),
      userRankView.getUiPoint(),
      userRankView.getUiTotalBook());
    }
}
