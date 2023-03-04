package com.readers.be3.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.readers.be3.service.ArticleService;
import com.readers.be3.vo.article.ArticleDetailVO;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.PatchCommentVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;
import com.readers.be3.vo.article.response.ArticleModifyResponse;
import com.readers.be3.vo.article.response.ArticleSearchResponseVO;
import com.readers.be3.vo.article.response.CommentResponse;
import com.readers.be3.vo.article.response.ResponseMessageVO;
import com.readers.be3.vo.article.response.WriteArticleResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;

@Tag(name = "게시글 및 댓글" , description ="게시글 등록/수정/조회/검색/삭제, 댓글 등록/수정/삭제 API")
@RestController
@RequestMapping("/api")
public class ArticleAPIController {
    @Autowired ArticleService articleService;

    // 게시글 등록 api
    @Operation(summary = "게시글 등록 api", description = "등록할 게시글 내용을 form-data로 받습니다.")
    @PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WriteArticleResponseVO> writeArticle(@ModelAttribute PostArticleVO data){
        WriteArticleResponseVO response = articleService.writeArticle(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시글 목록 조회및 검색 api
    @Operation(summary = "게시글 목록 조회 api", description = "게시글 리스트와 페이지 정보를 보여줍니다. type으로 검색 타입을 지정하고 keyword로 검색할 수 있습니다. *등록일 기준 내림차순으로 정렬합니다")
    @GetMapping("/article/{type}")
    public ResponseEntity<List<ArticleSearchResponseVO>> searchArticle(
            @Parameter(description = "검색타입 all(전체), writer(작성자), title(제목), content(내용), book(isbn번호로 검색)", example = "all") @PathVariable String type,
            @Parameter(description = "검색어(type=all일 경우 사용하지 않습니다.)") @RequestParam(required = false, value = "keyword") String keyword,
            @Parameter(description = "page default=0", example = "0") @RequestParam @Nullable Integer page,
            @Parameter(description = "size defult=10", example = "10") @RequestParam @Nullable Integer size
            )
    {
        List<ArticleSearchResponseVO> response = articleService.getArticleList(type, keyword, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시글 상세조회 api
    @Operation(summary = "게시글 상세 조회", description = "게시글 번호(aiSeq)에 해당하는 게시글을 상세조회합니다.")
    @GetMapping("/article/detail")
    public ResponseEntity<ArticleDetailVO> detailArticle(
        @Parameter(description = "검색할 게시글 번호", example = "1") @RequestParam Long aiSeq){
            ArticleDetailVO response = articleService.getArticleDetailInfo(aiSeq);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // 게시글 수정api
    @Operation(summary = "게시글 수정 api", description = "등록된 게시글의 제목과 내용, 이미지, 공개,비공개 여부를 수정합니다.")
    @PatchMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleModifyResponse> modifyArticle(@ModelAttribute ArticleModifyVO data){
        ArticleModifyResponse response = articleService.modifyArticle(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 게시글 삭제
    @Operation(summary = "게시글 삭제 api", description = "게시글을 삭제합니다.")
    @PatchMapping("/article/delete")
    public ResponseEntity<ResponseMessageVO> deleteArticle(
        @Parameter(description = "현재 로그인한 사용자 번호") @RequestParam Long uiSeq,
        @Parameter(description = "삭제할 게시글 번호") @RequestParam Long aiSeq){
            ResponseMessageVO response = articleService.deleteArticle(uiSeq, aiSeq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 댓글 작성
    @Operation(summary = "댓글 작성 api", description = "댓글작성")
    @PostMapping("/article/comment")
    public ResponseEntity<CommentResponse> writeComment(
    @Parameter(description = "댓글을 달 게시글의 번호", example = "1", required = true) @RequestParam Long acAiSeq,    
    @Parameter(description = "현재 로그인된 사용자의 번호 ", example = "1", required = true) @RequestParam Long acUiSeq,
    @RequestBody PostWriterCommentVO data)
    {
        CommentResponse response = articleService.postComment(acAiSeq, acUiSeq, data);
    return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정 api", description = "댓글 수정")
    @PatchMapping("/article/comment")
    public ResponseEntity<CommentResponse> modifyComment(
        @Parameter(description = "현재 로그인한 사용자 번호") @RequestParam Long uiSeq,
        @Parameter(description = "수정할 댓글 번호") @RequestParam Long acSeq,
        @RequestBody PatchCommentVO data
    ){
        CommentResponse response = articleService.patchComment(uiSeq, acSeq, data);
    return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제 api", description = "댓글 삭제")
    @PatchMapping("/article/comment/delete")
    public ResponseEntity<ResponseMessageVO> deleteComment(
        @Parameter(description = "현재 로그인한 사용자 번호") @RequestParam Long uiSeq,
        @Parameter(description = "삭제할 댓글 번호") @RequestParam Long acSeq)
    {
        ResponseMessageVO response = articleService.deleteComment(uiSeq, acSeq);
        return new ResponseEntity<>(response, HttpStatus.OK);
        
    }

    // 게시글 추천/비추천
    @Operation(summary = " 게시글 추천/비추천 api", description = "게시글 추천/비추천")
    @PatchMapping("/article/comment/recommend")
    public ResponseEntity<ResponseMessageVO> recommendAriticle(
        @Parameter(description = "현재 로그인한 사용자 번호") @RequestParam Long arUiSeq,
        @Parameter(description = "추천/비추천할 게시글 번호") @RequestParam Long arAiSeq,
        @Parameter(description = "추천/비추천(1=추천/2=비추천)") @RequestParam Integer arStatus)
    {
        ResponseMessageVO response = articleService.recommendAriticle(arUiSeq, arAiSeq ,arStatus);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
