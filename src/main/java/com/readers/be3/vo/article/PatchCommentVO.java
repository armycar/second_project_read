package com.readers.be3.vo.article;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchCommentVO {

@Schema(description = "수정할 댓글 내용", example = "댓글을 수정할게요.", required = true)
private String content;
}
