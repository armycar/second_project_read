package com.readers.be3.vo.mypage;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserImageVO {
    @Schema(description = "유저번호", example = "2") 
    private Long uiSeq; 
    private String filename;
    private String uri;
    private MultipartFile img;
}
