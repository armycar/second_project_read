package com.readers.be3.vo.mypage;

import java.time.LocalDateTime;

import com.readers.be3.entity.ArticleView;
import com.readers.be3.entity.OneCommentView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseUserArticleVO {
    @Schema(description = "독후감 번호")
    private Long aiSeq;
    @Schema(description = "독후감 제목" , example = "어린왕자를 읽고")
    private String title;
    @Schema(description = "독후감 내용" , example = "번역이 너무 잘되있고 , 재밌는 책이었습니다")
    private String content;
    @Schema(description = "독후감 날짜" , example = "2023-02-16 17:00:00")
    private LocalDateTime articleTime;
    
    @Schema(description = "한줄평 번호")
    private Long ocSeq;
    @Schema(description = "한줄평 내용" , example = "재밌어요")
    private String comment;
    @Schema(description = "한줄평 별점" , example = "5")
    private Integer score;
    @Schema(description = "한줄평 날짜" , example = "2023-02-16 17:00:00")
    private LocalDateTime commentTime;

    // private String title;

    // private String uri;


// 게시판 제목, 게시판 내용, 게시판 날짜
// 한줄평 내용, 한줄평 별점, 한줄평 날짜

    public ResponseUserArticleVO(ArticleView articleView) {
        this.title = articleView.getAiTitle();
        this.content = articleView.getAiContent();
        this.articleTime = articleView.getAiRegdt();
    }
    public ResponseUserArticleVO(OneCommentView oneCommentView) {
        this.comment = oneCommentView.getOcComment();
        this.score = oneCommentView.getOcScore();
        this.commentTime = oneCommentView.getOcRegdt();
    }
    public ResponseUserArticleVO(ArticleView articleView, OneCommentView oneCommentView) {
        this.title = articleView.getAiTitle();
        this.content = articleView.getAiContent();
        this.articleTime = articleView.getAiRegdt();
        this.comment = oneCommentView.getOcComment();
        this.score = oneCommentView.getOcScore();
        this.commentTime = oneCommentView.getOcRegdt();
    }
}

