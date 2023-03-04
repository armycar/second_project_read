package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.UserRankView;

public interface UserRankViewRepository extends JpaRepository<UserRankView,Long>{

  UserRankView findByUiSeq(Long uiSeq);

  
}
