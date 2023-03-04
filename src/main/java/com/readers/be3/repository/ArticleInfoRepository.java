package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.vo.article.GetSearchArticleVO;

public interface ArticleInfoRepository extends JpaRepository<ArticleInfoEntity, Long>{
    //게시글 리스트 조회
    @Query("SELECT a FROM ArticleInfoEntity a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1")
    public Page<GetSearchArticleVO> findAllArtilce(Pageable pageable);
    // 제목으로 게시글 검색
    @Query("SELECT a FROM ArticleInfoEntity a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.aiTitle LIKE %:keyword%")
    public Page<GetSearchArticleVO> findByAiTitleContains(@Param("keyword") String keyword, Pageable pageable);
    // 내용으로 게시글 검색
    @Query("SELECT a FROM ArticleInfoEntity a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.aiContent LIKE %:keyword%")
    public Page<GetSearchArticleVO> findByAiContentContains(@Param("keyword") String keyword, Pageable pageable);
    
    // 회원번호로 회원이 작성한 게시글 끌고오기
    public List<ArticleInfoEntity> findByAiUiSeq(Long uiSeq);
    // aiSeq로 게시글 가져오기(게시글 수정, 상세조회에 사용)
    public ArticleInfoEntity findByAiSeq(Long aiSeq); 
    // aiUiSeq
    // public ArticleInfoEntity fidBi

    
    // public Page<ArticleInfoEntity> findByiUiSeqContains(String keyword, Pageable pageable);
    // public Page<ArticleInfoEntity> findByAiTitleContains(String keyword, Pageable pageable);

    public ArticleInfoEntity findByAiUiSeqAndAiBiSeq(Long aiUiSeq, Long aiBiSeq);
}
