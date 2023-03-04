package com.readers.be3.vo.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.readers.be3.vo.book.ResponseBookInfoVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "일정을 추가할 때 ")
public class AddScheduleVO {
    @Schema(description = "회원 번호", example = "2")
    private Long uiSeq;
    // @Schema(description = "상태(1:읽기전 3:계획 4:완독)", example = "1")
    // private Integer status;
    @Schema(description = "책 번호", example = "1")
    private Long biSeq;
    @Schema(description = "시작 일(nullable)", example = "2023-02-07 01:23:45")
    private LocalDate start;
    @Schema(description = "종료 일(완독 일, nullable)", example = "2023-02-14 01:23:45")
    private LocalDate end;
    @Schema(description = "개인 작성(nullable)", example = "감동적이었습니다.")
    private String description;

    public AddScheduleVO(ResponseBookInfoVO data) {
        this.uiSeq = data.getUiSeq();
        this.biSeq = data.getBiSeq();
    }
    public AddScheduleVO() {}
}
