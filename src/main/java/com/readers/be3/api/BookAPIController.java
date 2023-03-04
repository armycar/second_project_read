package com.readers.be3.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.BookService;
import com.readers.be3.service.ScheduleService;
import com.readers.be3.utilities.ResponseMessageUtils;
import com.readers.be3.vo.book.BookInfoAladinVO;
import com.readers.be3.vo.book.GetMyBookVO;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.ResponseBookInfoVO;
import com.readers.be3.vo.response.BasicResponse;
import com.readers.be3.vo.schedule.AddScheduleVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 정보 관리", description = "회원별 도서 정보(내 서재) 추가/조회/변경/삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mybook")
public class BookAPIController {
    private final BookService bookService;
    private final ScheduleService scheduleService;
    // @Value("${file.image.book}") String book_img_path;

    @Operation(summary = "책 정보 추가", description = "aladin 검색 api를 통해 책 정보를 받아와서 추가합니다. DB에 없는 책은 DB에 저장후 리턴, 있는 책은 저장값을 리턴합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @PostMapping(value = "/add/{type}")
    public ResponseEntity<ResponseBookInfoVO> addBookInfo(
        @Parameter(description = "완독여부 정보를 받습니다.(plan : 읽을예정, read : 완독)", example = "plan") @PathVariable String type,
        @Parameter(description = "회원 번호", example = "110") @RequestParam Long uiSeq,
        @Parameter(description = "알라딘 검색 api를 통해 데이터를 받아옵니다. DB에 없는 책은 DB에 저장후 리턴, 있는 책은 저장값을 리턴합니다.") @RequestBody BookInfoAladinVO data
    ) {
        Integer status = -1;
        if      (type.equals("plan")) status = 1;
        else if (type.equals("read")) status = 4;
        else    throw new InvalidInputException("유효하지 않은 status type입니다.");
        ResponseBookInfoVO vo = bookService.addBookInfo(data);
        vo.setStatus(status);
        vo.setUiSeq(uiSeq);

        AddScheduleVO sVO = new AddScheduleVO(vo);
        scheduleService.addInitSchedule(sVO, status);
        return new ResponseEntity<>(vo, HttpStatus.CREATED);
    }

    @Operation(summary = "책 정보 조회", description = "회원이 추가한 책 목록을 조건에 따라 출력합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @GetMapping(value = "/list/{type}")
    public ResponseEntity<GetMyBookVO> getBookList(
        @Parameter(description = "완독여부 정보를 받습니다.(all : 전체, plan : 읽을예정, ing : 계획(달력에 추가), read : 완독)", example = "all") @PathVariable String type,
        @Parameter(description = "회원 번호", example = "110") @RequestParam Long uiSeq
    ) {
        Integer status = -1;
        if (type.equals("all")) status = 0;
        else if (type.equals("read")) status = 4;
        else if (type.equals("ing")) status = 3;
        else if (type.equals("plan")) status = 1;
        else throw new InvalidInputException("유효하지 않은 type입니다.");
        return new ResponseEntity<>(bookService.getMyBookList(uiSeq, status), HttpStatus.OK);
    }

    @Operation(summary = "완독 상태 변경", description = "내 서재에 있는 책을 완독으로 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @PostMapping(value = "/status")
    public ResponseEntity<BasicResponse> patchBookStatus(@RequestParam Long id) {
        return new ResponseEntity<>(bookService.patchBookStatus(id), HttpStatus.CREATED);
    }

    @Operation(summary = "등록된 책 삭제", description = "내 서재에 등록된 책을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @DeleteMapping(value = "/delete")
    public ResponseEntity<BasicResponse> deleteBook(@RequestParam Long id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
}
