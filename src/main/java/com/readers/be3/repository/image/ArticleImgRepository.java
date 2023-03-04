package com.readers.be3.repository.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.vo.article.GetImgInfoVO;

public interface ArticleImgRepository extends JpaRepository<ArticleImgEntity, Long> {
    public List<GetImgInfoVO> findByAimgAiSeq(Long aimgAiSeq);
    public ArticleImgEntity findByAimgUriEquals(String aimgUri);
    
}
