package com.readers.be3.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.image.UserImgEntity;

public interface UserImgRepository extends JpaRepository<UserImgEntity, Long>{
    public UserImgEntity findByUimgUiSeq(Long uimgUiSeq); 
    public UserImgEntity findTopByUimgUriEquals(String uimgUri);
}
