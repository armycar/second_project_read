package com.readers.be3.vo.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "일정을 수정할 때")
public class UpdateScheduleVO {
    @Schema(description = "일정 고유 번호", example = "6")
    private Long id;
    @Schema(description = "사용자 작성", example = "그저 그랬다")
    private String siContent;
    @Schema(description = "일정 시작 일", example = "2023-02-07 01:23:45")
    private LocalDate start;
    @Schema(description = "일정 종료 일", example = "2023-03-03 01:23:45")
    private LocalDate end;
}
