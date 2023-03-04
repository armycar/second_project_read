package com.readers.be3.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.ScheduleService;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.response.BasicResponse;
import com.readers.be3.vo.schedule.AddScheduleVO;
import com.readers.be3.vo.schedule.UpdateScheduleVO;
import com.readers.be3.vo.schedule.ViewScheduleVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "독서 계획 관리", description = "계획 추가, 조회, 수정, 삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleAPIController {
    private final ScheduleService scheduleService;

    @Operation(summary = "새 계획 추가", description = "내 서재에 있는 책을 계획으로 추가합니다.")
    @PostMapping("/add")
    public ResponseEntity<ViewScheduleVO> addSchedule(
        @Parameter(description = "스케쥴 입력 양식") @RequestBody AddScheduleVO data
    ) {
        if (data.getBiSeq()==null) {
            throw new InvalidInputException("책 번호 값이 없습니다.");
        }
        else if (data.getUiSeq()==null) {
            throw new InvalidInputException("회원 번호 값이 없습니다.");
        }
        else {
            return new ResponseEntity<>(scheduleService.addSchedule(data), HttpStatus.OK);
        }
    }

    @Operation(summary = "계획 삭제", description = "이 책을 내 서재에서 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<BasicResponse> deleteSchedule(
        @Parameter(description = "삭제할 일정 번호") @RequestParam Long id
    ) {
        return new ResponseEntity<>(scheduleService.deleteSchedule(id), HttpStatus.OK);
    }

    @Operation(summary = "계획 조회", description = "해당 회원이 등록한 모든 계획을 조회합니다.(status가 3인것만 조회)")
    @GetMapping("/my")
    public ResponseEntity< List<ViewScheduleVO> > getSchedule(
        @Parameter(description = "회원 번호", example = "110") @RequestParam Long uiSeq
    ) {
        return new ResponseEntity<>(scheduleService.getSchedule(uiSeq), HttpStatus.OK);
    }

    @Operation(summary = "계획 수정", description = "계획을 수정합니다.")
    @PostMapping("/update")
    public ResponseEntity<ViewScheduleVO> updateSchedule(
        @Parameter(description = "계획 수정 양식") @RequestBody UpdateScheduleVO data
    ){
        return new ResponseEntity<>(scheduleService.updateSchedule(data), HttpStatus.CREATED);
    }

    @Operation(summary = "완독 상태 변경", description = "이 책을 완독으로 수정합니다.")
    @PostMapping("/status")
    public ResponseEntity<BasicResponse> patchScheduleStatus(
        @Parameter(description = "계획 번호") @RequestParam Long id
    ){
        return new ResponseEntity<>(scheduleService.patchScheduleStatus(id), HttpStatus.OK);
    }
}
