package com.readers.be3.vo.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoVO {
@Schema(description = "유저이메일", example = "user01@naver.com")    
private String uiEmail;
@Schema(description = "유저비밀번호", example = "12341234") 
private String uiPwd;
// @Schema(description = "유저닉네임", example = "user01") 
// private String uiNickname;
}
