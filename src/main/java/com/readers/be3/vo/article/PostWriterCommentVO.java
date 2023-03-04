package com.readers.be3.vo.article;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostWriterCommentVO {
    @Schema(description = "댓글 내용", example = "testContent", required = true)
    private String content;
    
}
