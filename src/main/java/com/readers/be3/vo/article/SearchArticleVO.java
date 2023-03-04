package com.readers.be3.vo.article;

import java.time.LocalDateTime;

public interface SearchArticleVO {
public Long getUiSeq();
public String getUiEmail();
public String getUiPwd();
public String getUiNickname();
public LocalDateTime getUiRegDt();
public Integer getUiPoint();
public Integer getUiTotalPage();
public Integer getUiTotalBook();
public String getUiUid();
public String getUiLoginType();
}
