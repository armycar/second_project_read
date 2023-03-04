package com.readers.be3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.comments.CommentType;

import com.readers.be3.dto.request.OneCommentViewsDTO;
import com.readers.be3.dto.response.OneCommentListDTO;
import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.OneCommentEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.exception.ErrorResponse;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.OneCommentRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class OneCommentService {
  
  @Autowired ScheduleInfoRepository scheduleInfoRepository;
  @Autowired OneCommentRepository oneCommentRepository;
  @Autowired UserInfoRepository useInfoRepository;
  @Autowired BookInfoRepository bookRepository;

  @Autowired RedisTemplate<String,String> redisTemplate;


  @Transactional
  public OneCommentEntity OneCommentAdd(Long uiSeq, Long bookSeq, String comment, Integer score){
    //유저 체크
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));

    BookInfoEntity bookInfoEntity = bookRepository.findByBiSeq(bookSeq);
    if (bookInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found book", bookSeq)));
    
    List<OneCommentEntity> oneCommentEntityList = oneCommentRepository.findByUserInfoEntityAndBookInfoEntity(userInfoEntity, bookInfoEntity);
    if (oneCommentEntityList.size() > 0)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED, String.format("already wrote oneComment")));
    OneCommentEntity oneCommentEntity = oneCommentRepository.save(OneCommentEntity.of(comment, score, userInfoEntity, bookInfoEntity));
    
    UserInfoEntity newEntity = new UserInfoEntity(userInfoEntity, 100);
    useInfoRepository.save(newEntity);

    return oneCommentEntity;
  }

  @Transactional
  public OneCommentEntity OneCommentDelete(Long uiSeq, Long oneCommentSeq) {
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));
    OneCommentEntity oneEntity = oneCommentRepository.findByOcSeqAndUserInfoEntity(oneCommentSeq, userInfoEntity);
    if(oneEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found oneComment", uiSeq)));
    oneEntity.setOcStatus(2);
    OneCommentEntity oneCommentEntity = oneCommentRepository.save(oneEntity);

    UserInfoEntity newEntity = new UserInfoEntity(userInfoEntity, -200);
    useInfoRepository.save(newEntity);

    return oneCommentEntity;
  }

  @Transactional
  public Page<OneCommentListDTO> oneCommentList(String isbn, Pageable pageable){
    BookInfoEntity bookInfoEntity = bookRepository.findByBiIsbnEquals(isbn);
    if (bookInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("%s not found book", isbn)));
    Page<OneCommentEntity> commentList = oneCommentRepository.findByBookInfoEntityAndOcStatus(bookInfoEntity,pageable, 1);
    Page<OneCommentListDTO> onecommentDto = commentList.map(e-> OneCommentListDTO.toDto(e));
    return onecommentDto;
  }

  public OneCommentEntity OneCommentUpdate(Long uiSeq, Long oneCommentSeq, String content, Integer score) {
    if (content == "" || content == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("update content is null or empty")));
    if (score > 5 || score < 0)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("The score value is greater than 5 or less than 0")));  
    UserInfoEntity userInfoEntity = useInfoRepository.findByUiSeq(uiSeq);
    if (userInfoEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found userSeq", uiSeq)));
    OneCommentEntity oneCommentEntity = oneCommentRepository.findByOcSeq(oneCommentSeq);
    if (oneCommentEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found commentSeq", oneCommentSeq)));
    if (oneCommentEntity.getOcComment().equals(content) && oneCommentEntity.getOcScore().equals(score))
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED,String.format("content and score is equals")));
    if(oneCommentEntity.getUserInfoEntity().equals(oneCommentSeq))
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.CREATED,String.format("not my comment")));
    return oneCommentRepository.save(OneCommentEntity.update(oneCommentEntity, content, score));
  }

  public OneCommentViewsDTO getOneComment(Long oneCommentSeq) {
    // Find a  onecomment and check if it exists or not
    OneCommentEntity oneCommentEntity = oneCommentRepository.findByOcSeq(oneCommentSeq); 
    if (oneCommentEntity == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   not found commentSeq", oneCommentSeq)));
    OneCommentViewsDTO commentDTO = OneCommentViewsDTO.toDto(oneCommentEntity);
    if (commentDTO.getCommentViews() > 100 && commentDTO.getRegDt().plusDays(1).isAfter(LocalDateTime.now())){
      commentDTO.setCommentViews(setViewsByRedis(oneCommentSeq));
      return commentDTO;
    }
    System.out.println("들어갑니까?");
    oneCommentEntity.increaseViews();
    return OneCommentViewsDTO.toDto(oneCommentRepository.save(oneCommentEntity));
  }

  private Integer setViewsByRedis(Long oneCommentSeq) {
    final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    String viewValue = valueOperations.get("OneCommentSeq :/" + oneCommentSeq);
    if (viewValue == null || viewValue.equals("0")){
      valueOperations.set("OneCommentSeq :/" + oneCommentSeq, "101");
      return 1;
    }
    String tmpview = valueOperations.get("OneCommentSeq :/" + oneCommentSeq);
    Integer view = Integer.valueOf(tmpview);
    view += 1;
    valueOperations.set("OneCommentSeq :/" + oneCommentSeq, view.toString());
    return view;
  }

  // private boolean viewDuplicateCheck(Cookie[] cookies, Long oneCommentSeq) {
  //   Cookie viewCookie = null;
  //   if (cookies != null && cookies.length > 0){
  //     for(int i = 0; i < cookies.length; i++){
  //       if (cookies[i].getName().equals("cookie" + oneCommentSeq)){
  //         return true;
  //       }
  //     }
  //   }
  //   for(Cookie data: cookies){
  //     data.get
  //   }
  // }
}



