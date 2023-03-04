package com.readers.be3.vo.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchBookStatusVO { // 안 쓰이게 됨
    // @Schema(description = "유저 번호", example = "110")
    // private Long uiSeq;
    // @Schema(description = "책 고유번호", example = "1")
    // private Long biSeq;
    @Schema(description = "일정 고유번호", example = "36")
    private Long id;
    @Schema(description = "완독 여부(1.읽기전, 3.독서계획, 4.완독)", example = "4")
    private Integer status;
}
