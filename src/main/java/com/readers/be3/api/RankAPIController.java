package com.readers.be3.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.dto.response.UserRankDTO;
import com.readers.be3.service.RankService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "랭크", description = "랭크 api")
@RestController
@RequestMapping("/api/rank")
public class RankAPIController {
  

  @Autowired RankService rankService;

  @Operation(summary = "랭크 리스트", description = "랭크 리스트 출력")
  @GetMapping("/list")
  public ResponseEntity<Page<UserRankDTO>> getList(@Parameter(description = "페이지", example = "0") @RequestParam Integer page){
    Sort sort2 = Sort.by("uiPoint").descending();
    Pageable pageable = PageRequest.of(page, 10, sort2);
    return new ResponseEntity<>(rankService.getList(pageable),HttpStatus.OK); 
  }
  @Operation(summary = "자기 랭크 조회", description = "자기 랭크 조회")
  @GetMapping("/my")
  public ResponseEntity<UserRankDTO> getMyRank(@Parameter(description = "유저번호", example = "35") @RequestParam Long uiSeq){
    return new ResponseEntity<>(rankService.getMyRank(uiSeq),HttpStatus.OK);
  }
}
