package com.readers.be3.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.UserInfoService;
import com.readers.be3.vo.mypage.RequestUserVO;
import com.readers.be3.vo.mypage.ResponseUserArticleVO;
import com.readers.be3.vo.mypage.ResponseUserInfoVO;
import com.readers.be3.vo.mypage.SnsLoginRequest;
import com.readers.be3.vo.mypage.SnsLoginResponse;
import com.readers.be3.vo.mypage.UserImageVO;
import com.readers.be3.vo.mypage.UserInfoVO;
import com.readers.be3.vo.mypage.UserLoginVO;
import com.readers.be3.vo.mypage.UserNameVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "회원 관리와 마이페이지", description="마이페이지 CRUD API")
@RestController
@RequestMapping("/api/user")
public class UserInfoController {
    @Value("${file.image.user}") String user_img_path;
    @Autowired UserInfoService uService;

    @Operation(summary = "회원 가입", description = "회원정보를 추가합니다.")
    @PutMapping("/join") //회원가입
    public ResponseEntity<RequestUserVO> userJoin(@RequestBody UserInfoVO data) {
      return new ResponseEntity<>(uService.addUser(data), HttpStatus.OK);
    }

    @Operation(summary = "일반 로그인", description = "로그인")
    @PostMapping("/login") //로그인
    public ResponseEntity<RequestUserVO> userLogin(@RequestBody UserLoginVO data) {
      return new ResponseEntity<>(uService.loginUser(data), HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "회원정보 번호(uiSeq)를 통해 회원의 상태를 탈퇴처리로 변경합니다")
    @PatchMapping("/leave") //회원탈퇴
    public ResponseEntity<RequestUserVO> userDelete(
      @Parameter(description = "회원번호", example = "130") @RequestParam Long uiSeq) throws Exception{
    return new ResponseEntity<>(uService.leaveUser(uiSeq), HttpStatus.OK);
  }

  @Operation(summary = "회원 정보 수정", description = "회원정보 번호(uiSeq)를 통해 회원정보(닉네임)를 수정합니다.")
    @PatchMapping("/update/name") //회원정보수정
    public ResponseEntity<RequestUserVO> userNameUpdate(
      @Parameter(description = "회원번호", example = "87") @RequestParam Long uiSeq, @RequestBody UserNameVO data) throws Exception{
    return new ResponseEntity<>(uService.updateUserName(uiSeq, data), HttpStatus.OK);
  }

  @Operation(summary = "회원 사진추가", description = "회원정보 번호(uiSeq)를 통해 회원사진을 수정(추가)합니다. filename과 uri은 입력 안해도 됩니다")
    @PutMapping(value = "/update/photo", consumes= MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestUserVO> updateUserPhoto(
        @Parameter(description = "formdata로 사진 데이터를 입력합니다")
        @ModelAttribute UserImageVO data
    ) {
        return new ResponseEntity<>(uService.updateUserPhoto(data), HttpStatus.OK);
    }

  @Operation(summary = "회원 정보", description = "회원정보 번호(uiSeq)를 통해 회원의 정보를 조회합니다. 마이페이지 화면 중앙의 데이터 입니다")
    @GetMapping("/info") //회원정보 조회
    public ResponseEntity<ResponseUserInfoVO> getUserInfo(
      @Parameter(description = "회원번호", example = "87") @RequestParam Long uiSeq){
      return new ResponseEntity<>(uService.getUserInfo(uiSeq),HttpStatus.OK);
    }  

  // @Operation(summary = "완독서 관리", description = "마이페이지에서 회원정보 번호(uiSeq)를 통해 회원이 완독한 책을 조회합니다.")
  //   @GetMapping("/finish") //완독책 조회
  //   public ResponseEntity< List<ResponseFinishedBookVO> > getUserBook(
  //     @Parameter(description = "회원번호", example = "87") @RequestParam Long uiSeq){
  //     return new ResponseEntity<>(uService.getUserBook(uiSeq),HttpStatus.OK);
  //   }  

  @Operation(summary = "완독한 책의 평점, 독후감 확인", description = "회원정보 번호(uiSeq)와 도서 ISBN(isbn)을 통해 해당 도서의 평점과 독후감을 조회합니다.")
    @GetMapping("/article") //평점, 독후감 확인
    public ResponseEntity<ResponseUserArticleVO> getUserArticle(
      @Parameter(description = "회원번호", example = "37") @RequestParam Long uiSeq, 
      @Parameter(description = "책 ISBN", example = "9788979592566") @RequestParam String isbn){
      return new ResponseEntity<>(uService.getUserArticle(uiSeq, isbn), HttpStatus.OK);
    }  

  @Operation(summary = "sns 로그인", description = "sns로 로그인합니다. 쎄션에 저장할 유저번호를 반환합니다")
  @PostMapping("/sns/login")
  public ResponseEntity<SnsLoginResponse> snsLogin(@RequestBody SnsLoginRequest request){
    return new ResponseEntity<>(SnsLoginResponse.toResponse(uService.snsloginUser(request.getSnsUid(), request.getType())),HttpStatus.OK);
  }

}
