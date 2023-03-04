package com.readers.be3.repository;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.SearchArticleView;
import com.readers.be3.vo.article.response.ArticleSearchResponseVO;

public interface SearchArticleViewRepository extends JpaRepository<SearchArticleView, Integer>{
// 모든 게시글 검색
@Query("SELECT a FROM SearchArticleView a")
public List<ArticleSearchResponseVO> findAll(PageRequest pageRequest);
// 작성자(닉네임)로 게시글 검색
@Query("SELECT a FROM SearchArticleView a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.uiNickname like %:keyword% ")
public List<ArticleSearchResponseVO> searchNickname(@Param("keyword") String keyword, PageRequest pageRequest);
// 내용으로 검색
@Query("SELECT a FROM SearchArticleView a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.aiContent  like %:keyword% ")
public List<ArticleSearchResponseVO> searchContent(@Param("keyword") String keyword, PageRequest pageRequest);
// 제목으로 게시글 검색
@Query("SELECT a FROM SearchArticleView a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.aiTitle  like %:keyword% ")
public List<ArticleSearchResponseVO> searchTitle(@Param("keyword") String keyword, PageRequest pageRequest);
// isbn으로 게시글 검색
@Query("SELECT a FROM SearchArticleView a WHERE a.aiStatus = 1 AND a.aiPurpose = 1 AND a.aiPublic = 1 AND a.biIsbn like %:keyword% ")
public List<ArticleSearchResponseVO> searchIsbn(@Param("keyword") String keyword, PageRequest pageRequest);
}
