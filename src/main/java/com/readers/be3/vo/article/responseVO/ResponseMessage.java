package com.readers.be3.vo.article.responseVO;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseMessage {
    @Schema(description = "상태", example = "false")  
    private boolean status;
    @Schema(description = "메세지", example = "잘못된 접근입니다")  
    private String message;
    @Schema(description = "메세지")
    private HttpStatus code;
    
    // 성공시 출력되는 메세지
    public ResponseMessage(String message, HttpStatus code){
        this.status = true;
        this.message  = "";
        this.code  = code;
    }

    // // 실패시 출력되는 메세지
    // public ResponseMessage(String result){
    //     this.reason = MessageUtils.FAIL;
    //     this.result = result;
    // }

}
