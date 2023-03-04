package com.readers.be3.vo.book;

import com.readers.be3.entity.BookInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseBookInfoVO {
    @Schema(description = "유저 고유번호", example = "110")
    private Long uiSeq;
    @Schema(description = "상태(1:읽기전 3:계획 4:완독)", example = "1")
    private Integer status;
    @Schema(description = "책 고유번호", example = "1")
    private Long biSeq;
    @Schema(description = "제목", example = "아라비안 나이트 5 - 상인 알라딘, 문화")
    private String biName;
    @Schema(description = "저자", example = "그림나무 글.그림, 이희수 감수")
    private String biAuthor;
    @Schema(description = "출판사", example = "기탄출판")
    private String biPublisher;
    @Schema(description = "총 페이지 수", example = "185")
    private Integer biPage;
    @Schema(description = "ISBN", example = "9788979592566 ")
    private String biIsbn;
    @Schema(description = "책 이미지 URI", example = "http://image.aladdin.co.kr/coveretc/book/coversum/8979592566_1.jpg")
    private String bimgUri;

    // public ResponseBookInfoVO(BookInfoImgVO data) {
    //     this.biName = data.getBiName();
    //     this.biAuthor = data.getBiAuthor();
    //     this.biPublisher = data.getBiPublisher();
    //     this.biPage = data.getBiPage();
    //     this.biIsbn = data.getBiIsbn();
    // }
    public ResponseBookInfoVO(BookInfoEntity data) {
        this.biSeq = data.getBiSeq();
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
        this.bimgUri = data.getBiUri();
    }
    public ResponseBookInfoVO(BookInfoAladinVO data) {
        // this.uiSeq = data.getUiSeq();
        // this.status = data.getStatus();
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
        this.bimgUri = data.getBimgUri();
    }
    public ResponseBookInfoVO() {}
}
