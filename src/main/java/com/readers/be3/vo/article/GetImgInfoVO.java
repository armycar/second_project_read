package com.readers.be3.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;

public interface GetImgInfoVO {

@Schema(description = "파일명")
public String getAimgFilename();
@Schema(description = "파일uri")
public String getAimgUri();

}
