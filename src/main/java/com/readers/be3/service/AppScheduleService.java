package com.readers.be3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.readers.be3.dto.response.OneCommentListDTO;
import com.readers.be3.entity.OneCommentEntity;
import com.readers.be3.repository.OneCommentRepository;

import jakarta.transaction.Transactional;

@Service
public class AppScheduleService {
  
  @Autowired RedisTemplate<String,String> redisTemplate;
  @Autowired OneCommentRepository oneCommentRepository;

  @Transactional
  public void oneCommentViewsSchedule(){
  final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
  LocalDateTime oneDayBefore = LocalDateTime.now().minusDays(1);
   List<OneCommentEntity> commentList = oneCommentRepository.findByOcViewsGreaterThanAndOcRegDtAfterAndOcStatus(100L, oneDayBefore, 1);

    for(OneCommentEntity data : commentList){
      String value = valueOperations.get("OneCommentSeq :/" + data.getOcSeq());
      if (value == null)
        continue;
      else {
        data.setOcViews(Integer.parseInt(value));
        // valueOperations.set("OneCommentSeq :/" + data.getOcSeq(), "0");
        oneCommentRepository.save(data);
      }
    }

  }
}
