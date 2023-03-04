package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ArticleCommentEntity;
import com.readers.be3.vo.article.GetCommentVO;

public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, Long>{
    public ArticleCommentEntity findByAcSeq(Long acSeq);
      // 게시글에 달린 댓글 가져오기(상태값 1인 것만)
      public List<GetCommentVO> findByAcAiSeqAndAcStatus(Long aiSeq, Integer acStatus);
}