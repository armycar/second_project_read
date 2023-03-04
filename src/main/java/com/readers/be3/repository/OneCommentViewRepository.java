package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.OneCommentView;

public interface OneCommentViewRepository extends JpaRepository<OneCommentView, Long>{
    OneCommentView findByUiSeqAndBiSeq(Long uiSeq, Long biSeq);
}
