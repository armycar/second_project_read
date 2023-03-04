package com.readers.be3.vo.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SnsLoginRequest {

  @Schema(description = "sns uid " , example = "gds543")
  private String snsUid;
  @Schema(description = "snsType" , example = "kakao")
  private String type;
}
