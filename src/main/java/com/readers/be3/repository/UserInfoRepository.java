package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.UserInfoEntity;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long>{
  public Integer countByUiEmail(String uiEmail);
  public Integer countByUiNickname(String uiNickname);
  public Integer countByUiStatus(Integer uiStatus);
  public UserInfoEntity findTop1ByUiEmailAndUiPwd(String uiEmail, String uiPwd);
  public UserInfoEntity findByUiSeq(Long uiSeq);
  public Page<UserInfoEntity> findAll(Pageable pageable);
  public UserInfoEntity findByUiUidAndUiLoginType(String uid, String type);
  // 아이디로 유저 정보 찾기 (유저아이디로 게시글 검색을 위해서 만듦)
  public UserInfoEntity findByUiNickname(String nickName);
  public List<UserInfoEntity> findTop1ByUiStatus(Integer uiStatus);
  public List<UserInfoEntity> findByUiStatus(Integer uiStatus);
}
