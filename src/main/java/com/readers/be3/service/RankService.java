package com.readers.be3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.UserRankViewRepository;
import com.readers.be3.dto.response.UserRankDTO;
import com.readers.be3.entity.UserRankView;
import com.readers.be3.exception.ErrorResponse;
import com.readers.be3.exception.ReadersProjectException;

@Service
public class RankService {

  @Autowired UserInfoRepository UserInfoRepository;
  @Autowired UserRankViewRepository userRankViewRepository;

  public Page<UserRankDTO> getList(Pageable pageable) {
    return  userRankViewRepository.findAll(pageable).map(e->UserRankDTO.toResponse(e));
  }

  
  public UserRankDTO getMyRank(Long uiSeq) {
    UserRankView userRankView= userRankViewRepository.findByUiSeq(uiSeq);
    if (userRankView == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
    return UserRankDTO.toResponse(userRankView);
  }
  
}
