package com.readers.be3.vo.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserNameVO {
    @Schema(description = "유저닉네임", example = "user01")
    private String uiNickname;
}
