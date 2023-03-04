package com.readers.be3.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.response.BasicResponse;
import com.readers.be3.vo.schedule.AddScheduleVO;
import com.readers.be3.vo.schedule.UpdateScheduleVO;
import com.readers.be3.vo.schedule.ViewScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserInfoRepository userInfoRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    public List<ViewScheduleVO> getSchedule(Long uiSeq) {
        List<ViewScheduleVO> list = new ArrayList<ViewScheduleVO>();
        userInfoRepository.findById(uiSeq).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원번호 입니다."));
        if (scheduleInfoRepository.findBySiUiSeq(uiSeq).size()==0) {
            throw new NoSuchElementException();
        }
        for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeq(uiSeq)) {
            ViewScheduleVO vo = new ViewScheduleVO(entity);
            list.add(vo);
        }
        return list;
    }

    public ViewScheduleVO addInitSchedule(AddScheduleVO data, Integer status) {
        UserInfoEntity uEntity = userInfoRepository.findById(data.getUiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원 번호 입니다."));
        BookInfoEntity bEntity = bookInfoRepository.findById(data.getBiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 책 번호 입니다."));
        ViewScheduleVO vo = new ViewScheduleVO();
        LocalDate sDate = null;
        LocalDate eDate = null;

        if (scheduleInfoRepository.findBySiUiSeqAndSiBiSeq(data.getUiSeq(), data.getBiSeq())!=null) {
            throw new InvalidInputException("이미 내 서재에 등록된 책입니다.");
        }
        vo.setTitle(bEntity.getBiName());
        vo.setDescription(data.getDescription());
        vo.setStart("");
        vo.setEnd("");
        vo.setStatus(status);

        ScheduleInfoEntity entity = ScheduleInfoEntity.builder()
                .siContent(data.getDescription())
                .siStartDate(sDate)
                .siEndDate(eDate)
                .siStatus(status)
                .siUiSeq(data.getUiSeq())
                .siBiSeq(data.getBiSeq()).build();

        scheduleInfoRepository.save(entity);
        vo.setId(entity.getSiSeq());
        
        Integer page = bEntity.getBiPage();
        if (status==4) {
            UserInfoEntity newEntity = new UserInfoEntity(uEntity, true, page);
            userInfoRepository.save(newEntity);
        }
        return vo;
    }

    public ViewScheduleVO addSchedule(AddScheduleVO data) {
        userInfoRepository.findById(data.getUiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원 번호 입니다."));
        bookInfoRepository.findById(data.getBiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 책 번호 입니다."));
        ViewScheduleVO vo = new ViewScheduleVO();
        LocalDate sDate = null;
        LocalDate eDate = null;
        if (data.getStart()!=null) {
            sDate = data.getStart();
        }
        if (data.getEnd()!=null) {
            eDate = data.getEnd();
        }
        Integer status = 3;
        if ((sDate!=null && eDate!=null) && sDate.isAfter(eDate)) {
            throw new InvalidInputException("종료일은 시작일보다 빠를 수 없습니다.");
        }
        vo.setTitle(bookInfoRepository.findById(data.getBiSeq()).get().getBiName());
        vo.setDescription(data.getDescription());
        vo.setStart(sDate.toString());
        vo.setEnd(eDate.toString());
        vo.setStatus(status);

        ScheduleInfoEntity entity = ScheduleInfoEntity.builder()
                .siContent(data.getDescription())
                .siStartDate(sDate)
                .siEndDate(eDate)
                .siStatus(status)
                .siUiSeq(data.getUiSeq())
                .siBiSeq(data.getBiSeq()).build();

        scheduleInfoRepository.save(entity);
        vo.setId(entity.getSiSeq());
        return vo;
    }

    public BasicResponse deleteSchedule(Long id) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(id).orElseThrow(() -> new InvalidInputException("존재하지 않는 계획입니다."));
        UserInfoEntity uEntity = userInfoRepository.findById(entity.getSiUiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원 번호 입니다."));
        BookInfoEntity bEntity = bookInfoRepository.findById(entity.getSiBiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 책 번호 입니다."));
        Integer status = entity.getSiStatus();
        scheduleInfoRepository.delete(entity);
        if (status==4) {
            Integer page = bEntity.getBiPage();
            UserInfoEntity newEntity = new UserInfoEntity(uEntity, false, page);
            userInfoRepository.save(newEntity);
        }
        return new BasicResponse("true", "계획을 삭제했습니다.");
    }

    public ViewScheduleVO updateSchedule(UpdateScheduleVO data) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(data.getId())
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 계획입니다."));
        
        Integer status = entity.getSiStatus();
        if (status!=3) {
            throw new InvalidInputException("계획 상태의 책이 아닙니다.");
        }
        Long uiSeq = entity.getSiUiSeq();
        Long biSeq = entity.getSiBiSeq();
        LocalDate sDate = null;
        LocalDate eDate = null;
        if (data.getStart()!=null) {
            sDate = data.getStart();
        }
        if (data.getEnd()!=null) {
            eDate = data.getEnd();
        }
        if ((data.getStart()!=null && data.getEnd()!=null) && sDate.isAfter(eDate)) {
            throw new InvalidInputException("종료일은 시작일보다 빠를 수 없습니다.");
        }

        ScheduleInfoEntity newEntity = ScheduleInfoEntity.builder()
                .siSeq(data.getId())
                .siContent(data.getSiContent())
                .siStartDate(sDate)
                .siEndDate(eDate)
                .siUiSeq(uiSeq).siBiSeq(biSeq)
                .siStatus(status).build();
        scheduleInfoRepository.save(newEntity);

        ViewScheduleVO responseVO = new ViewScheduleVO();
        responseVO.setId(newEntity.getSiSeq());
        responseVO.setTitle(bookInfoRepository.findByBiSeq(newEntity.getSiBiSeq()).getBiName());
        responseVO.setDescription(newEntity.getSiContent());
        responseVO.setStart(newEntity.getSiStartDate().toString());
        responseVO.setEnd(newEntity.getSiEndDate().toString());
        responseVO.setStatus(status);
        return responseVO;
    }

    public BasicResponse patchScheduleStatus(Long id) {
        if (scheduleInfoRepository.findById(id).isEmpty()) {
            throw new InvalidInputException("존재하지 않는 정보입니다.");
        }
        else if (scheduleInfoRepository.findById(id).get().getSiStatus()==4) {
            throw new InvalidInputException("유효하지 않은 접근입니다.(이미 완독한 책)");
        }
        else {
            ScheduleInfoEntity entity = scheduleInfoRepository.findById(id).get();
            UserInfoEntity uEntity = userInfoRepository.findById(entity.getSiUiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원 번호 입니다."));
            BookInfoEntity bEntity = bookInfoRepository.findById(entity.getSiBiSeq()).orElseThrow(() -> new InvalidInputException("존재하지 않는 책 번호 입니다."));
            ScheduleInfoEntity newEntity = ScheduleInfoEntity.builder()
                    .siSeq(entity.getSiSeq())
                    .siContent(entity.getSiContent())
                    .siStartDate(entity.getSiStartDate())
                    .siEndDate(entity.getSiEndDate())
                    .siUiSeq(entity.getSiUiSeq())
                    .siBiSeq(entity.getSiBiSeq())
                    .siStatus(4).build();
            scheduleInfoRepository.save(newEntity);

            Integer page = bEntity.getBiPage();
            UserInfoEntity newUserEntity = new UserInfoEntity(uEntity, true, page);
            userInfoRepository.save(newUserEntity);
        }
        return new BasicResponse("true", "상태값이 완독으로 변경됐습니다.");
    }
}
