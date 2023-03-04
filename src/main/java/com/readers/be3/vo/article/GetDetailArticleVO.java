package com.readers.be3.vo.article;

import java.time.LocalDateTime;
import java.util.List;

import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.entity.ArticleCommentEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetDetailArticleVO {
    
    private Long aiSeq;
    private String aiTitle;
    private String aiContent;
    private LocalDateTime aiRegDt;
    private LocalDateTime aiModDt;
    private Integer aiStatus;
    private Integer aiPurpose;
    private Integer aiPublic;
    private Integer aiBiSeq;
    private Integer aiUiSeq;
    private List<ArticleImgEntity> articleImgEntity;
    private List<ArticleCommentEntity> ArticleCommentEntity;


}
