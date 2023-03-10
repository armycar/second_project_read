package com.readers.be3.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.readers.be3.entity.ArticleView;
import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.MyPageView;
import com.readers.be3.entity.OneCommentView;
import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.UserImgEntity;
import com.readers.be3.exception.ErrorResponse;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.ArticleInfoRepository;
import com.readers.be3.repository.ArticleViewRepository;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.MyPageViewRepository;
import com.readers.be3.repository.OneCommentRepository;
import com.readers.be3.repository.OneCommentViewRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.image.UserImgRepository;
import com.readers.be3.utilities.AESAlgorithm;
import com.readers.be3.utilities.RandomNameUtils;
import com.readers.be3.vo.mypage.RequestUserVO;
import com.readers.be3.vo.mypage.ResponseFinishedBookVO;
import com.readers.be3.vo.mypage.ResponseUserArticleVO;
import com.readers.be3.vo.mypage.ResponseUserInfoVO;
import com.readers.be3.vo.mypage.UserImageVO;
import com.readers.be3.vo.mypage.UserInfoVO;
import com.readers.be3.vo.mypage.UserLoginVO;
import com.readers.be3.vo.mypage.UserNameVO;

@Service
public class UserInfoService {
    @Autowired UserImgRepository i_repo;
    @Autowired UserInfoRepository u_repo;
    @Autowired ScheduleInfoRepository scheduleInfoRepository;
    @Autowired MyPageViewRepository v_repo;
    @Autowired ArticleViewRepository a_repo;
    @Autowired OneCommentViewRepository o_repo;
    @Autowired BookInfoRepository b_repo;
    @Autowired OneCommentRepository oneCommentRepository;
    @Autowired ArticleInfoRepository articleInfoRepository;
    
    @Value("${file.image.user}") String user_img_path;

    public RequestUserVO addUser(UserInfoVO data) { //????????????, Response ??????
    // String name_pattern = "^[0-9|a-z|A-Z|???-???|???-???|???-???]*$";
    String pwd_pattern = "^[a-zA-Z0-9!@#$%^&*()-_=+]*$";
    RequestUserVO response = new RequestUserVO();

    Long num = Calendar.getInstance().getTimeInMillis();
          
    if(u_repo.countByUiEmail(data.getUiEmail())>=1) { 
       response = RequestUserVO.builder()
        .status(false)
        .message("?????? ????????? ??????????????????")
        .build();
    }
    else if(data.getUiPwd().length()<8) { 
      response = RequestUserVO.builder()
       .status(false)
       .message("??????????????? 8?????? ???????????????")
       .build();
   }
    else if(!Pattern.matches(pwd_pattern, data.getUiPwd())) {
      response = RequestUserVO.builder()
       .status(false)
       .message("??????????????? ??????????????? ????????? ??? ????????????")
       .build();
    } 
    else {
      try {
        String encPwd = AESAlgorithm.Encrypt(data.getUiPwd());
        data.setUiPwd(encPwd);
      }  catch(Exception e) {e.printStackTrace();}
      UserInfoEntity member = UserInfoEntity.builder()
        .uiPwd(data.getUiPwd())
        .uiEmail(data.getUiEmail())
        .uiNickname("user#"+ num)
        .build();

        u_repo.save(member);

        response = RequestUserVO.builder()
        .status(true)
        .message("????????? ?????????????????????")
        .build();
    }
    return response;
    }

    public UserInfoEntity snsloginUser(String uid, String type) { //?????????
      UserInfoEntity userInfoEntity = u_repo.findByUiUidAndUiLoginType(uid, type);
      if (userInfoEntity == null){
        UserInfoEntity snsUser = UserInfoEntity.ofSNS(uid, type);
        return  u_repo.save(snsUser);
      }
      return userInfoEntity;
  }
    
    public RequestUserVO loginUser(UserLoginVO data) { //?????? ?????????
        UserInfoEntity loginUser = null; 
        RequestUserVO response = new RequestUserVO();
    try {
      loginUser = u_repo.findTop1ByUiEmailAndUiPwd(
      data.getUiEmail(), AESAlgorithm.Encrypt(data.getUiPwd())
      );
    }catch(Exception e) {e.printStackTrace();}
    if(loginUser == null) {
      response = RequestUserVO.builder()
      .status(false)
      .message("????????? ?????? ???????????? ???????????????")
      .build();
    }
    else if(loginUser.getUiStatus() != 1) {
      response = RequestUserVO.builder()
      .status(false)
      .message("??????????????? ???????????????")
      .build();
    }
    else {
      response = RequestUserVO.builder()
      .uiSeq(loginUser.getUiSeq())
      .status(true)
      .message("????????? ???????????????")
      .build();
    }
        return response;
    }

    public RequestUserVO leaveUser(Long uiSeq) { //????????????
        RequestUserVO response = new RequestUserVO();
        UserInfoEntity login = u_repo.findByUiSeq(uiSeq);
        if(login == null) {
          response = RequestUserVO.builder()
          .status(false)
          .message("????????? ???????????????")
          .build();
        }
        else if(login.getUiStatus() == 2) {
          response = RequestUserVO.builder()
          .status(false)
          .message("?????? ??????????????? ????????? ???????????????")
          .build();
        }
        else {
          Date now = new Date();
        login.setUiStatus(2); //????????? ??????
        login.setUiLeaveDt(now);
        u_repo.save(login); //????????? ??? ??????
        response = RequestUserVO.builder()
          .status(true)
          .message("??????????????? ?????????????????????")
          .build();
        }
    return response;
}

    public RequestUserVO updateUserName(Long uiSeq, UserNameVO data) { //????????? ??????
        String name_pattern = "^[0-9|a-z|A-Z|???-???|???-???|???-???]*$";
        RequestUserVO response = new RequestUserVO();
        UserInfoEntity login = u_repo.findByUiSeq(uiSeq);
        if(login == null) {
          response = RequestUserVO.builder()
          .status(false)
          .message("????????? ???????????????")
          .build();
        }
        else if(u_repo.countByUiNickname(data.getUiNickname())>=1&&!login.getUiNickname().equals(data.getUiNickname())) { 
          response = RequestUserVO.builder()
          .status(false)
          .message(data.getUiNickname()+"???/??? ?????? ????????? ????????? ?????????")
          .build();
        }
        else if(!Pattern.matches(name_pattern, data.getUiNickname())) {
        response = RequestUserVO.builder()
          .status(false)
          .message(data.getUiNickname()+"???????????? ??????????????? ??????????????? ?????? ??? ??? ????????????")
          .build();
        } 
        else if(data.getUiNickname()=="") {
          response = RequestUserVO.builder()
          .status(false)
          .message(data.getUiNickname()+"????????? ???????????????")
          .build();
        }
        else {
        login.setUiNickname(data.getUiNickname());
        u_repo.save(login);
        response = RequestUserVO.builder()
        .status(true)
        .message("????????? ????????? ?????? ???????????????")
        .build();
        }
        return response;
    }

  public RequestUserVO updateUserPhoto(UserImageVO data) { // ?????? ?????? ??????
    RequestUserVO response = new RequestUserVO();
    UserInfoEntity login = u_repo.findByUiSeq(data.getUiSeq());
    UserImgEntity img = i_repo.findByUimgUiSeq(data.getUiSeq());
  

      if(img==null) { //????????? ?????? ???????????? , ???????????? ????????????

      String originalFileName = data.getImg().getOriginalFilename();
      String[] split = originalFileName.split("\\.");
      String ext = split[split.length - 1];
      String filename = "";
      for (int i=0; i<split.length-1; i++) {
        filename += split[i];
      }
      String saveFilename = "user_" + Calendar.getInstance().getTimeInMillis() + "."+ext;
      
      Path forderLocation = Paths.get(user_img_path);
      Path targetFile = forderLocation.resolve(saveFilename);

      try {
        Files.copy(data.getImg().getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
      }
      catch (Exception e) {
        response = RequestUserVO.builder()
          .status(false)
          .message("??????????????? ??????????????????")
          .build();
      }

      UserImgEntity imgEntity = UserImgEntity.builder()
      .uimgFilename(saveFilename)
      .uimgUri(RandomNameUtils.MakeRandomUri("first", data.getUiSeq()))
      .uimgUiSeq(login.getUiSeq()).build();

      i_repo.save(imgEntity);
      response = RequestUserVO.builder()
          .status(true)
          .message("?????? ????????? ?????????????????????")
          .build();
    } 
    else { // ????????? ?????? ???????????? ???????????? ????????? ???????????????
      i_repo.delete(img);
      String originalFileName = data.getImg().getOriginalFilename();
      String[] split = originalFileName.split("\\.");
      String ext = split[split.length - 1];
      String filename = "";
      for (int i=0; i<split.length-1; i++) {
        filename += split[i];
      }
      String saveFilename = "user_" + Calendar.getInstance().getTimeInMillis() + "."+ext;
      
      Path forderLocation = Paths.get(user_img_path);
      Path targetFile = forderLocation.resolve(saveFilename);

      try {
        Files.copy(data.getImg().getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
      }
      catch (Exception e) {
        response = RequestUserVO.builder()
          .status(false)
          .message("??????????????? ??????????????????")
          .build();
      }

      UserImgEntity imgEntity = UserImgEntity.builder()
      .uimgFilename(saveFilename)
      .uimgUri(RandomNameUtils.MakeRandomUri("update", data.getUiSeq()))
      .uimgUiSeq(login.getUiSeq()).build();

      i_repo.save(imgEntity);
      response = RequestUserVO.builder()
          .status(true)
          .message("?????? ????????? ?????????????????????")
          .build();
    }
    return response;
  }

  public ResponseUserInfoVO getUserInfo(Long uiSeq) { //??????????????? ????????????
    MyPageView mView= v_repo.findByUiSeq(uiSeq);
    if (mView == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
    return ResponseUserInfoVO.toResponse(mView);
  }

  public List<ResponseFinishedBookVO> getUserBook(Long uiSeq) { //????????? ??? ??????
    List<ResponseFinishedBookVO> list = new ArrayList<ResponseFinishedBookVO>();
    for (ScheduleInfoEntity data : scheduleInfoRepository.findBySiUiSeqAndSiStatus(uiSeq, 4)) {
      if (data == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
      ResponseFinishedBookVO vo = new ResponseFinishedBookVO(data);
      list.add(vo);
    }
    return list;
  }

  public ResponseUserArticleVO getUserArticle(Long uiSeq, String isbn) { //??????????????? ????????? ??? ???????????? ?????? ??????
    Long biSeq = b_repo.findByBiIsbnEquals(isbn).getBiSeq();
    if (biSeq==null) biSeq = -1L;
    ArticleView aView = a_repo.findByUiSeqAndBiSeq(uiSeq, biSeq);
    OneCommentView oView = o_repo.findByUiSeqAndBiSeq(uiSeq, biSeq);
    // if (aView == null && oView ==null) {
      // throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found  uiSeq : %d , biSeq : %d ", uiSeq, biSeq)));
    // }
    Long aiSeq = null;
    Long ocSeq = null;
    if (articleInfoRepository.findByAiUiSeqAndAiBiSeq(uiSeq, biSeq)!=null) aiSeq = articleInfoRepository.findByAiUiSeqAndAiBiSeq(uiSeq, biSeq).getAiSeq();
    if (oneCommentRepository.findByOcUiSeqAndOcBiSeq(uiSeq, biSeq)!=null)  ocSeq = oneCommentRepository.findByOcUiSeqAndOcBiSeq(uiSeq, biSeq).getOcSeq();

    if (aView==null && oView==null) {
      ResponseUserArticleVO vo = new ResponseUserArticleVO();
      return vo;
    }
    else if (aView==null) {
      ResponseUserArticleVO vo = new ResponseUserArticleVO(oView);
      vo.setOcSeq(ocSeq);
      return vo;
    }
    else if (oView==null) {
      ResponseUserArticleVO vo = new ResponseUserArticleVO(aView);
      vo.setAiSeq(aiSeq);
      return vo;
    }
    else {
      ResponseUserArticleVO vo = new ResponseUserArticleVO(aView, oView);
      vo.setAiSeq(aiSeq);
      vo.setOcSeq(ocSeq);
      return vo;
    }
  }
}

