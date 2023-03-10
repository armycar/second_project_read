package com.readers.be3.service;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.readers.be3.exception.ErrorResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.type.descriptor.java.LocalDateJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.ArticleCommentEntity;
import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.entity.ArticleRecommendEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.ArticleCommentRepository;
import com.readers.be3.repository.ArticleInfoRepository;
import com.readers.be3.repository.ArticleRecommendRepository;
import com.readers.be3.repository.SearchArticleViewRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.ArticleImgRepository;
import com.readers.be3.utilities.RandomNameUtils;
import com.readers.be3.vo.article.ArticleDetailVO;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.GetCommentVO;
import com.readers.be3.vo.article.GetImgInfoVO;
import com.readers.be3.vo.article.PatchCommentVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;
import com.readers.be3.vo.article.response.ArticleModifyResponse;
import com.readers.be3.vo.article.response.ArticleSearchResponseVO;
import com.readers.be3.vo.article.response.CommentResponse;
import com.readers.be3.vo.article.response.ResponseMessageVO;
import com.readers.be3.vo.article.response.WriteArticleResponseVO;
import com.readers.be3.vo.book.InvalidInputException;

@Service
public class ArticleService {
    @Autowired ArticleImgRepository articleImgRepo;
    @Autowired ArticleInfoRepository articleInfoRepo;
    @Autowired UserInfoRepository userInfoRepo;
    @Autowired SearchArticleViewRepository searchArticleRepo;
    @Autowired ArticleCommentRepository articleCommentRepo;
    @Autowired ArticleRecommendRepository articleRecommendRepo;
    
    @Value("${file.image.article}") String ArticleImgPath;

    // ????????? ?????? 
    public WriteArticleResponseVO writeArticle(PostArticleVO data){
        // VO??? ?????? ????????? ????????? ??????, ??????(?????????)??? ????????????
        ArticleInfoEntity articleInfoEntity = null;
        WriteArticleResponseVO response = null;
        UserInfoEntity user = userInfoRepo.findByUiSeq(Long.valueOf(data.getUiSeq()));
        // ????????? ???????????? ???????????? ???????????? ??????
        if(user == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("???????????? ????????????.")));
        else if(data.getAiTitle() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("????????? ???????????????")));
        
        else if(data.getContent() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("????????? ???????????????")));
        
        else if(data.getAiPublic() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("??????????????? ???????????????(1.?????? / 2.?????????)")));

        else{
            // ????????? ??????
            articleInfoEntity = ArticleInfoEntity.builder()
                                .aiTitle(data.getAiTitle())
                                .aiContent(data.getContent())
                                .aiPublic(data.getAiPublic())
                                .aiUiSeq(data.getUiSeq())
                                .aiBiSeq(data.getBiSeq())
                                .aiRegDt(LocalDateTime.now())
                                .aiPurpose(1)
                                .aiStatus(1)
                                .build();
            articleInfoRepo.save(articleInfoEntity);

            // ????????? ????????? ?????? ????????? ??????
            UserInfoEntity newEntity = new UserInfoEntity(user, 500);
            userInfoRepo.save(newEntity);

            // ????????? ??????
            try{
                imgfileHandler(data.getFiles(), articleInfoEntity.getAiSeq());
            }
            catch(Exception e){
            e.printStackTrace();
            }
            List<GetImgInfoVO> imgFiles = articleImgRepo.findByAimgAiSeq(articleInfoEntity.getAiSeq());
            // ResponseMessageVO 
            response = WriteArticleResponseVO.builder()
                                .aiSeq(articleInfoEntity.getAiSeq())
                                .aiTitle(articleInfoEntity.getAiTitle())
                                .aiContent(articleInfoEntity.getAiContent())
                                .aiPublic(articleInfoEntity.getAiPublic())
                                .aiUiSeq(articleInfoEntity.getAiUiSeq())
                                .aiBiSeq(articleInfoEntity.getAiBiSeq())
                                .aiRegDt(articleInfoEntity.getAiRegDt())
                                .aiPurpose(articleInfoEntity.getAiPurpose())
                                .aiStatus(articleInfoEntity.getAiStatus())
                                .imgFiles(imgFiles)
                                .build();
        }
        return response;
    }

    // ????????? ?????? ?????? ?????????
   public void imgfileHandler(List<MultipartFile> files, Long aiSeq) throws Exception {
    if (!CollectionUtils.isEmpty(files)) {
        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) { // ????????? ???????????? ?????????
                String contentType = multipartFile.getContentType();
                String originalFileExtension = "";

                if (ObjectUtils.isEmpty(contentType))  // ??????????????? ?????????(????????? ??????)
                    break;
                 else {
                    if      (contentType.contains("image/jpeg"))    originalFileExtension = "jpg";
                    else if (contentType.contains("image/png"))     originalFileExtension = "png";
                    else if (contentType.contains("image/gif"))     originalFileExtension = "gif";
                    else  break;
                    }

                String newFileName = "article_" + Calendar.getInstance().getTimeInMillis() + "." + originalFileExtension;
                ArticleImgEntity articleImgEntity = ArticleImgEntity.builder()
                        .aimgFilename(newFileName)
                        .aimgAiSeq(aiSeq)
                        .aimgUri(RandomNameUtils.MakeRandomUri(originalFileExtension, aiSeq))
                        .build();
                articleImgRepo.save(articleImgEntity);

                File file = new File(ArticleImgPath);
                // ????????? ????????? ??????????????? ???????????? ?????? ?????? ??????????????? ??????
                if (!file.exists())  file.mkdirs(); 
                
               Path savePath = Paths.get(ArticleImgPath+File.separator+newFileName);
                multipartFile.transferTo(savePath);
            }
        }
        System.out.println("???????????? ?????????????????????.");
    } else {
        System.out.println("????????? ???????????? ????????????.");
    }
}

// ????????? ??????
// ??????(?????????, ??????, ??????)
// pathvarible ??? ????????????(?????? ?????????, ?????????, ??????, ?????? )
// type => (all, writer, title, content, book)
public List<ArticleSearchResponseVO> getArticleList(String type, String keyword, Integer page ,Integer size){
    List<ArticleSearchResponseVO> response = null;
    // ???????????? : ????????????, ?????????
    // ???????????? ?????? arStatus
    // if(sort == null) sort = "aiRegDt";
    if(page == null) page = 0;
    if(size == null) size = 10;
    PageRequest pageRequest = PageRequest.of(page,size,Sort.by("aiRegDt").descending());

    // ????????? ?????? ??????
if(type.equals("all"))
     response = searchArticleRepo.findAll(pageRequest);
    // ???????????? ??????(?????????)
else if(type.equals("writer"))
     response = searchArticleRepo.searchNickname(keyword, pageRequest);
 // ???????????? ??????
else if(type.equals("title"))
     response = searchArticleRepo.searchTitle(keyword, pageRequest);
 // ???????????? ??????
else if(type.equals("content"))
     response = searchArticleRepo.searchContent(keyword, pageRequest);
// ISBN?????? ??????
else if(type.equals("book"))
    response = searchArticleRepo.searchIsbn(keyword, pageRequest);
else{
    throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("????????? ???????????? ?????????.")));
}
return response;
}

// ????????? ????????????
public ArticleDetailVO getArticleDetailInfo(Long aiSeq){
    ArticleInfoEntity detailInfo = articleInfoRepo.findByAiSeq(aiSeq);
    List <GetCommentVO> showComment = articleCommentRepo.findByAcAiSeqAndAcStatus(aiSeq, 1);
    List <GetImgInfoVO> showImgInfo = articleImgRepo.findByAimgAiSeq(aiSeq);
    ArticleDetailVO response = null;
    if(detailInfo == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ?????? ??????????????????.")));
    
    else if(detailInfo.getAiPublic() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ??????????????????.")));
     
    else if(detailInfo.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ????????? ??????????????????.")));
     
    else{
        response = ArticleDetailVO.builder()
        .aiSeq(detailInfo.getAiSeq())
        .aiTitle(detailInfo.getAiTitle())
        .content(detailInfo.getAiContent())
        .regDt(detailInfo.getAiRegDt())
        .aiModDt(detailInfo.getAiModDt())
        .aiStatus(detailInfo.getAiStatus())
        .aiPurpose(detailInfo.getAiPurpose())
        .aiPublic(detailInfo.getAiPublic())
        .biSeq(detailInfo.getAiBiSeq())
        .uiSeq(detailInfo.getAiUiSeq())
        .showImgInfo(showImgInfo)
        .showComment(showComment)
        .build();
    } 
    return response;
}

// ????????? ??????
public ArticleModifyResponse modifyArticle(ArticleModifyVO data){
    ArticleInfoEntity modifyPost = null;
    ArticleModifyResponse response = null;
    // ????????? ???????????? ??????
    modifyPost = articleInfoRepo.findByAiSeq(data.getAiSeq());
    // ?????????
    LocalDateTime modifyDate = LocalDateTime.now();
    if(modifyPost == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ???????????? ?????????.")));
    
    else if(modifyPost.getAiUiSeq() != data.getUiSeq().intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ??????????????? ???????????? ????????? ??? ?????????.")));
    
    else if(modifyPost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ????????? ????????? ?????????.")));

    else{
        if (data.getAiTitle() != null)     modifyPost.setAiTitle(data.getAiTitle());
        if (data.getContent() != null)     modifyPost.setAiContent(data.getContent());
        if (data.getAiPublic() != null)    modifyPost.setAiPublic(data.getAiPublic());
        if (data.getFiles() != null){
            try{
                imgfileHandler(data.getFiles(), data.getAiSeq());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        modifyPost.setAiModDt(modifyDate);
        articleInfoRepo.save(modifyPost);
        List<GetImgInfoVO> fileInfo = articleImgRepo.findByAimgAiSeq(modifyPost.getAiSeq());
        response = ArticleModifyResponse.builder()
        .aiSeq(modifyPost.getAiSeq())
        .aiTitle(modifyPost.getAiTitle())
        .content(modifyPost.getAiContent())
        .aiPublic(modifyPost.getAiPublic())
        .uiSeq(modifyPost.getAiUiSeq())
        .files(fileInfo)
        .build();

    }
    return response;
}

// ????????? ??????
public ResponseMessageVO deleteArticle(Long uiSeq, Long aiSeq){
    ResponseMessageVO response = null;
    ArticleInfoEntity deletePost = null;
    deletePost = articleInfoRepo.findByAiSeq(aiSeq);
    if(Objects.isNull(deletePost))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ???????????? ?????????.")));
    
    else if(deletePost.getAiUiSeq() != uiSeq.intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ??????????????? ???????????? ????????? ??? ?????????.")));

    else if(deletePost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ????????? ??????????????????.")));
    
    else{
        deletePost.setAiStatus(2);
        articleInfoRepo.save(deletePost);

        // ????????? ????????? ?????? ????????? ??????
        UserInfoEntity uEntity = userInfoRepo.findById(uiSeq).orElseThrow(() -> new InvalidInputException("???????????? ?????? ???????????????."));
        UserInfoEntity newEntity = new UserInfoEntity(uEntity, -600);
        userInfoRepo.save(newEntity);

        response = ResponseMessageVO.builder()
        .status(true)
        .message(" ???????????? ?????????????????????.")
        .build();
    }
    return response;
}

//?????? ??????
public CommentResponse postComment(Long acAiSeq, Long acUiSeq,PostWriterCommentVO data){
    ArticleCommentEntity comment = null;
    UserInfoEntity user = userInfoRepo.findByUiSeq(acUiSeq);
    ArticleInfoEntity article = articleInfoRepo.findByAiSeq(acAiSeq);
    CommentResponse response = null;
    
    if(data.getContent() == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("????????? ???????????????.")));
    else if(article == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("???????????? ?????? ??????????????????.")));
    else if(article.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("????????? ??????????????????.")));
    else if(user == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("???????????? ????????????.")));
    else{
        comment = ArticleCommentEntity.builder()
                                        .acContent(data.getContent())
                                        .acAiSeq(acAiSeq)
                                        .acUiSeq(acUiSeq)
                                        .build();
                                        articleCommentRepo.save(comment);

        LocalDateTime regDt = LocalDateTime.now();
        response = CommentResponse.builder()
                                        .acSeq(comment.getAcSeq())
                                        .acContent(comment.getAcContent())
                                        .acRegDt(regDt)
                                        .acModDt(comment.getAcModDt())
                                        .acStatus(1)
                                        .acAiSeq(comment.getAcAiSeq())
                                        .acUiSeq(comment.getAcUiSeq())
                                        .build();
    }
    return response;
}

// ?????? ??????
public CommentResponse patchComment(Long uiSeq, Long acSeq, PatchCommentVO data ){
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    CommentResponse response = null;
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("???????????? ????????????.")));
    
    else {
        ArticleCommentEntity commentInfo = articleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ?????? ???????????????.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ??????????????? ????????? ????????? ??? ?????????.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ????????? ???????????????.")));
        
        else if(data.getContent().isEmpty())
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("????????? ??????????????????.")));
        
        else{
            LocalDateTime modDt = LocalDateTime.now().withNano(0);
            
            commentInfo.setAcContent(data.getContent());
            commentInfo.setAcModDt(modDt);
            commentInfo.setAcAiSeq(commentInfo.getAcAiSeq());
            commentInfo.setAcUiSeq(commentInfo.getAcUiSeq());
            articleCommentRepo.save(commentInfo);

            response = CommentResponse.builder()
                .acSeq(commentInfo.getAcSeq())
                .acContent(commentInfo.getAcContent())
                .acRegDt(commentInfo.getAcRegDt())
                .acModDt(commentInfo.getAcModDt())
                .acStatus(commentInfo.getAcStatus())
                .acAiSeq(commentInfo.getAcAiSeq())
                .acUiSeq(commentInfo.getAcUiSeq())
                .build();
        }
    }
    return response;
}
        
        
// ?????? ??????
public ResponseMessageVO deleteComment(Long uiSeq, Long acSeq){
    ResponseMessageVO response = null;
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("???????????? ????????????.")));
    
    else {
        ArticleCommentEntity commentInfo = articleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ?????? ???????????????.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ??????????????? ????????? ????????? ??? ?????????.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ?????? ????????? ???????????????.")));
        
        else {
            commentInfo.setAcStatus(2);
            articleCommentRepo.save(commentInfo);
            response = ResponseMessageVO.builder()
            .message("????????? ???????????????.")
            .status(true)
            .build();
        }
        }
    return response;
    }

    // ????????? ??????/?????????
    public ResponseMessageVO recommendAriticle(Long arUiSeq, Long arAiSeq, Integer arStatus){
        ResponseMessageVO response = null;
        ArticleRecommendEntity count = null;
        // ???????????? ??????????????? ??????
        ArticleInfoEntity article = articleInfoRepo.findByAiSeq(arAiSeq);
        // ????????? ??????????????? ??????
        UserInfoEntity user = userInfoRepo.findByUiSeq(arUiSeq);
        if(user == null)            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ????????????.")));
        else if(article == null)    throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ???????????? ?????? ??????????????????.")));
        else if(article.getAiStatus() == 2)  throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" ????????? ??????????????????.")));
        else{
            count = ArticleRecommendEntity.builder()
            .arStatus(arStatus)
            .arAiSeq(arAiSeq)
            .arUiSeq(arUiSeq)
            .build();
            articleRecommendRepo.save(count);
            
            response = ResponseMessageVO.builder()
            .message("??????/???????????? ?????????????????????.")
            .status(true)
            .build();
        }
        return response;
    }

    
    
}
    

