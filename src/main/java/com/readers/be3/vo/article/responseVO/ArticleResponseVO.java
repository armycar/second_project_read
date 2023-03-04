package com.readers.be3.vo.article.responseVO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.readers.be3.entity.image.ArticleImgEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleResponseVO {
    @Schema(description = " 번호", example = "1")  
    private Long aiSeq;
    @Schema(description = " 제목", example = "실패")  
    private String aiTitle;
    @Schema(description = " 내용", example = "실패")  
    private String aiContent;
    @Schema(description = " 등록일", example = "실패")  
    private LocalDateTime aiRegDt;
    @Schema(description = "수정일", example = "실패")  
    private LocalDateTime aiModDt;
    @Schema(description = "삭제유무(1.정상 2.삭제)", example = "1") 
    private Integer aiStatus;
    @Schema(description = "상태값(1.게시판 2.리뷰 3. 이벤트)", example = "1") 
    private Integer aiPurpose;
    @Schema(description = "상태값(1.공개 2.비공개)", example = "1") 
    private Integer aiPublic;
    @Schema(description = "책 번호", example = "1") 
    private Integer aiBiSeq;
    @Schema(description = "회원 번호(작성자)", example = "1") 
    private Integer aiUiSeq;
    @Schema(description = "이미지 파일") 
    private List<ArticleImgEntity> articleImgEntity;
}
