package com.readers.be3.vo.mypage;
import com.readers.be3.entity.BookInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseBookInfoVO {
    @Schema(description = "책제목" , example = "달과 6펜스")
    private String bookTitle;
    @Schema(description = "책사진" , example = "달과 6펜스.jpg")
    private String uri;

    public ResponseBookInfoVO(BookInfoEntity bInfoEntity) {
        this.bookTitle = bInfoEntity.getBiName();
        this.uri = bInfoEntity.getBiUri();
    }
}
