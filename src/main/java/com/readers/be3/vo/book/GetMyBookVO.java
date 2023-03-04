package com.readers.be3.vo.book;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyBookVO {
    @Schema(description = "유저 번호", example = "110")
    private Long uiSeq;
    @Schema(description = "책 목록(시간 역순)")
    private List<GetBookListVO> mybookList;
}
