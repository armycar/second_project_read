package com.readers.be3.vo.article.response;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResponseMessageVO {
    @Schema(description = "메시지", example = "성공적으로 처리했습니다.")
    private String message;
    @Schema(description = "성공여부(T/F", example = "true")
    private boolean status;
    }
