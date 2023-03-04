package com.readers.be3.vo.article.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.readers.be3.vo.article.GetImgInfoVO;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WriteArticleResponseVO {
    @Schema(description = "게시글 번호", example = "1")  
    private Long aiSeq;
    @Schema(description = "게시글 제목", example = "안녕하세요")  
    private String aiTitle;
    @Schema(description = "게시글 내용", example = "반갑습니다")  
    private String aiContent;
    @Schema(description = "등록일", example = "")  
    private LocalDateTime aiRegDt;
    @Schema(description = "수정일", example = "null")  
    private LocalDateTime aiModDt;
    @Schema(description = "상태(1.정상 2.삭제)", example = "1")  
    private Integer aiStatus;
    @Schema(description = "상태(1.게시판 2.리뷰 3. 이벤트)", example = "1")  
    private Integer aiPurpose;
    @Schema(description = "상태(1.공개 2.비공개)", example = "1")  
    private Integer aiPublic;
    @Schema(description = "책번호", example = "1")  
    private Long aiBiSeq;
    @Schema(description = "회원 번호", example = "1")  
    private Long aiUiSeq;
    @Schema(description = "업로드한 파일 목록")
    private List<GetImgInfoVO> imgFiles;

    
}
