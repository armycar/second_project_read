package com.readers.be3.vo.article;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

// 게시글 수정할 데이터
@Data
public class ArticleModifyVO {

@Schema(description = "게시글 제목", example = "게시글 제목을 수정했어요", required = true)
private String aiTitle;
@Schema(description = "게시글 내용", example = "게시글을 수정했어요", required = true)
private String content;
@Schema(description = "상태(1.공개 2.비공개)", example = "1", required = true)
private Integer aiPublic;
@Schema(description = "회원 번호", example = "1", required = true)
private Long uiSeq;
@Schema(description = "게시글 번호", example = "1", required = true)
private Long aiSeq;
@Schema(description = "첨부 파일 목록")
private List<MultipartFile> files;
}
