package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ArticleView;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Long>{
    ArticleView findByUiSeqAndBiSeq(Long uiSeq, Long biSeq);
}
