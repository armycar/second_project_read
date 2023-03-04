package com.readers.be3.utilities;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.UserImgEntity;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.UserImgRepository;

import java.util.*;

@Component
public class DeleteMemberInfo {
    @Autowired UserInfoRepository u_repo;
    @Autowired UserImgRepository i_repo;
    
    @Scheduled(cron = "45 33 17 * * *") //매일 12시 0분 00초 실행
    public void autoDelete() {

        for (UserInfoEntity member : u_repo.findByUiStatus(2)) {
            
            if(member != null) {
                UserImgEntity img = i_repo.findByUimgUiSeq(member.getUiSeq());

                    Date day = new Date();
                    Calendar today = Calendar.getInstance();
                    today.setTime(day); 
                    
                    Calendar leaveDt = Calendar.getInstance();
                    
                    leaveDt.setTime(member.getUiLeaveDt());
                    Long diffSec = (today.getTimeInMillis() - leaveDt.getTimeInMillis())/1000;
                    Long diffDay = diffSec / (24 * 60 * 60);
                    
                    if(diffDay >= 180) {
                        i_repo.delete(img);
                        u_repo.delete(member);
                    }
                    else 
                    continue;
                }
                else if(member == null) {                    
                    break;
                }
            }
        }
     }

