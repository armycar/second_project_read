package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.MyPageView;

public interface MyPageViewRepository extends JpaRepository<MyPageView,Long>{

  MyPageView findByUiSeq(Long uiSeq);

  
}
