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

    // 게시글 작성 
    public WriteArticleResponseVO writeArticle(PostArticleVO data){
        // VO를 통해 게시글 제목과 내용, 파일(이미지)을 입력받음
        ArticleInfoEntity articleInfoEntity = null;
        WriteArticleResponseVO response = null;
        UserInfoEntity user = userInfoRepo.findByUiSeq(Long.valueOf(data.getUiSeq()));
        // 제목을 입력하지 않았다면 입력하게 처리
        if(user == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("로그인을 해주세요.")));
        else if(data.getAiTitle() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("제목을 입력하세요")));
        
        else if(data.getContent() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("내용을 입력하세요")));
        
        else if(data.getAiPublic() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("공개여부를 선택하세요(1.공개 / 2.비공개)")));

        else{
            // 게시글 저장
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

            // 게시글 작성에 따른 포인트 저장
            UserInfoEntity newEntity = new UserInfoEntity(user, 500);
            userInfoRepo.save(newEntity);

            // 이미지 저장
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

    // 이미지 파일 저장 메소드
   public void imgfileHandler(List<MultipartFile> files, Long aiSeq) throws Exception {
    if (!CollectionUtils.isEmpty(files)) {
        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) { // 파일이 비어있지 않다면
                String contentType = multipartFile.getContentType();
                String originalFileExtension = "";

                if (ObjectUtils.isEmpty(contentType))  // 확장자명이 없다면(잘못된 파일)
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
                // 저장할 위치의 디렉토리가 존재하지 않을 경우 디렉토리를 생성
                if (!file.exists())  file.mkdirs(); 
                
               Path savePath = Paths.get(ArticleImgPath+File.separator+newFileName);
                multipartFile.transferTo(savePath);
            }
        }
        System.out.println("이미지가 저장되었습니다.");
    } else {
        System.out.println("등록한 이미지가 없습니다.");
    }
}

// 게시글 조회
// 검색(작성자, 제목, 내용)
// pathvarible 로 검색타입(모든 게시글, 작성자, 제목, 내용 )
// type => (all, writer, title, content, book)
public List<ArticleSearchResponseVO> getArticleList(String type, String keyword, Integer page ,Integer size){
    List<ArticleSearchResponseVO> response = null;
    // 정렬기준 : 등록일순, 추천순
    // 추천수로 정렬 arStatus
    // if(sort == null) sort = "aiRegDt";
    if(page == null) page = 0;
    if(size == null) size = 10;
    PageRequest pageRequest = PageRequest.of(page,size,Sort.by("aiRegDt").descending());

    // 게시글 전체 검색
if(type.equals("all"))
     response = searchArticleRepo.findAll(pageRequest);
    // 작성자로 검색(닉네임)
else if(type.equals("writer"))
     response = searchArticleRepo.searchNickname(keyword, pageRequest);
 // 제목으로 검색
else if(type.equals("title"))
     response = searchArticleRepo.searchTitle(keyword, pageRequest);
 // 내용으로 검색
else if(type.equals("content"))
     response = searchArticleRepo.searchContent(keyword, pageRequest);
// ISBN으로 검색
else if(type.equals("book"))
    response = searchArticleRepo.searchIsbn(keyword, pageRequest);
else{
    throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("잘못된 검색타입 입니다.")));
}
return response;
}

// 게시글 상세조회
public ArticleDetailVO getArticleDetailInfo(Long aiSeq){
    ArticleInfoEntity detailInfo = articleInfoRepo.findByAiSeq(aiSeq);
    List <GetCommentVO> showComment = articleCommentRepo.findByAcAiSeqAndAcStatus(aiSeq, 1);
    List <GetImgInfoVO> showImgInfo = articleImgRepo.findByAimgAiSeq(aiSeq);
    ArticleDetailVO response = null;
    if(detailInfo == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 게시글이에요.")));
    
    else if(detailInfo.getAiPublic() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 비공개된 게시글이에요.")));
     
    else if(detailInfo.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글이에요.")));
     
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

// 게시글 수정
public ArticleModifyResponse modifyArticle(ArticleModifyVO data){
    ArticleInfoEntity modifyPost = null;
    ArticleModifyResponse response = null;
    // 수정할 게시글을 선택
    modifyPost = articleInfoRepo.findByAiSeq(data.getAiSeq());
    // 수정일
    LocalDateTime modifyDate = LocalDateTime.now();
    if(modifyPost == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 게시글이 존재하지 않아요.")));
    
    else if(modifyPost.getAiUiSeq() != data.getUiSeq().intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 게시글은 수정할 수 없어요.")));
    
    else if(modifyPost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글 입니다.")));

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

// 게시글 삭제
public ResponseMessageVO deleteArticle(Long uiSeq, Long aiSeq){
    ResponseMessageVO response = null;
    ArticleInfoEntity deletePost = null;
    deletePost = articleInfoRepo.findByAiSeq(aiSeq);
    if(Objects.isNull(deletePost))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 게시글이 존재하지 않아요.")));
    
    else if(deletePost.getAiUiSeq() != uiSeq.intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 게시글은 삭제할 수 없어요.")));

    else if(deletePost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글이에요.")));
    
    else{
        deletePost.setAiStatus(2);
        articleInfoRepo.save(deletePost);

        // 게시글 작성에 따른 포인트 저장
        UserInfoEntity uEntity = userInfoRepo.findById(uiSeq).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원입니다."));
        UserInfoEntity newEntity = new UserInfoEntity(uEntity, -600);
        userInfoRepo.save(newEntity);

        response = ResponseMessageVO.builder()
        .status(true)
        .message(" 게시글이 삭제되었습니다.")
        .build();
    }
    return response;
}

//댓글 작성
public CommentResponse postComment(Long acAiSeq, Long acUiSeq,PostWriterCommentVO data){
    ArticleCommentEntity comment = null;
    UserInfoEntity user = userInfoRepo.findByUiSeq(acUiSeq);
    ArticleInfoEntity article = articleInfoRepo.findByAiSeq(acAiSeq);
    CommentResponse response = null;
    
    if(data.getContent() == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("내용을 입력하세요.")));
    else if(article == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("존재하지 않는 게시글이에요.")));
    else if(article.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("삭제된 게시글이에요.")));
    else if(user == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
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

// 댓글 수정
public CommentResponse patchComment(Long uiSeq, Long acSeq, PatchCommentVO data ){
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    CommentResponse response = null;
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
    
    else {
        ArticleCommentEntity commentInfo = articleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 댓글이에요.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 댓글은 수정할 수 없어요.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 댓글이에요.")));
        
        else if(data.getContent().isEmpty())
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("내용을 입력해주세요.")));
        
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
        
        
// 댓글 삭제
public ResponseMessageVO deleteComment(Long uiSeq, Long acSeq){
    ResponseMessageVO response = null;
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
    
    else {
        ArticleCommentEntity commentInfo = articleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 댓글이에요.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 댓글은 삭제할 수 없어요.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 이미 삭제된 댓글이에요.")));
        
        else {
            commentInfo.setAcStatus(2);
            articleCommentRepo.save(commentInfo);
            response = ResponseMessageVO.builder()
            .message("댓글을 삭제했어요.")
            .status(true)
            .build();
        }
        }
    return response;
    }

    // 게시글 추천/비추천
    public ResponseMessageVO recommendAriticle(Long arUiSeq, Long arAiSeq, Integer arStatus){
        ResponseMessageVO response = null;
        ArticleRecommendEntity count = null;
        // 게시글이 존재하는지 검사
        ArticleInfoEntity article = articleInfoRepo.findByAiSeq(arAiSeq);
        // 로그인 되어있는지 검사
        UserInfoEntity user = userInfoRepo.findByUiSeq(arUiSeq);
        if(user == null)            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 로그인을 해주세요.")));
        else if(article == null)    throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 게시글이에요.")));
        else if(article.getAiStatus() == 2)  throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글이에요.")));
        else{
            count = ArticleRecommendEntity.builder()
            .arStatus(arStatus)
            .arAiSeq(arAiSeq)
            .arUiSeq(arUiSeq)
            .build();
            articleRecommendRepo.save(count);
            
            response = ResponseMessageVO.builder()
            .message("추천/비추천이 등록되었습니다.")
            .status(true)
            .build();
        }
        return response;
    }

    
    
}
    

