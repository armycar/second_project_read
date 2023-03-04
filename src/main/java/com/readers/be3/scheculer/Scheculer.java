package com.readers.be3.scheculer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.readers.be3.repository.OneCommentRepository;
import com.readers.be3.service.AppScheduleService;

@Component
public class Scheculer {

  @Autowired  AppScheduleService scheduleService;

  
  @Scheduled(fixedDelay = 12000)
  public  void scheduleSetView(){
    scheduleService.oneCommentViewsSchedule();
  }

  // @Scheduled(fixedDelay = )
}
