package com.readers.be3.vo.mypage;

import java.util.List;

import com.readers.be3.entity.MyPageView;
import com.readers.be3.entity.ScheduleInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseFinishedBookVO {
    @Schema(description = "책제목" , example = "달과 6펜스")
    private String bookTitle;
    @Schema(description = "작가이름" , example = "서머셋 몸")
    private String author;
    @Schema(description = "출판사명" , example = "민음사")
    private String publisher;
    @Schema(description = "책표지" , example = "달과 6펜스.jpg")
    private String uri;

    public ResponseFinishedBookVO (ScheduleInfoEntity data) {
        this.bookTitle = data.getBookInfoEntity().getBiName();
        this.author = data.getBookInfoEntity().getBiAuthor();
        this.publisher = data.getBookInfoEntity().getBiPublisher();
        this.uri = data.getBookInfoEntity().getBiUri();
    }
}
