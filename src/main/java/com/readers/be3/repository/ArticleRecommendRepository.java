package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ArticleRecommendEntity;

public interface ArticleRecommendRepository extends JpaRepository<ArticleRecommendEntity, Long>{
    
}
