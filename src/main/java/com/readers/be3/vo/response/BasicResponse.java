package com.readers.be3.vo.response;

import com.readers.be3.utilities.ResponseMessageUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasicResponse {
    @Schema(description = "성공(true)/실패(false)", example = "false")
    private String status;
    @Schema(description = "메시지", example = "message")
    private String message;

    public BasicResponse() {
        this.status = ResponseMessageUtils.TRUE;
        this.message = "데이터 없음";
    }
    public BasicResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public BasicResponse(String message) {
        this.status = ResponseMessageUtils.FALSE;
        this.message = message;
    }
}
