package com.readers.be3.vo.mypage;

import com.readers.be3.entity.UserInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SnsLoginResponse {
    @Schema(description = "유저번호", example = "12")    
    private Long uiSeq;
    @Schema(description = "sns타입", example = "kakao")    
    private String snsType;

    public static SnsLoginResponse toResponse(UserInfoEntity entity){
      return new SnsLoginResponse(entity.getUiSeq(), entity.getUiLoginType());
    }
}

