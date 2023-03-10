package com.readers.be3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.BookInfoAladinVO;
import com.readers.be3.vo.book.GetBookListVO;
import com.readers.be3.vo.book.GetMyBookVO;
import com.readers.be3.vo.book.ResponseBookInfoVO;
import com.readers.be3.vo.response.BasicResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final UserInfoRepository userInfoRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    public ResponseBookInfoVO addBookInfo(BookInfoAladinVO data) {
        if (bookInfoRepository.findByBiIsbnEquals(data.getBiIsbn())==null) {
            BookInfoEntity entity = BookInfoEntity.builder()
                    .biName(data.getBiName())
                    .biAuthor(data.getBiAuthor())
                    .biPublisher(data.getBiPublisher())
                    .biPage(data.getBiPage())
                    .biIsbn(data.getBiIsbn())
                    .biUri(data.getBimgUri()).build();
            bookInfoRepository.save(entity);
            ResponseBookInfoVO vo = new ResponseBookInfoVO(data);
            vo.setBiSeq(entity.getBiSeq());
            return vo;
        }
        else {
            ResponseBookInfoVO vo = new ResponseBookInfoVO(bookInfoRepository.findByBiIsbnEquals(data.getBiIsbn()));
            return vo;
        }
    }

    public GetMyBookVO getMyBookList(Long uiSeq, Integer status) {
        List<GetBookListVO> list = new ArrayList<GetBookListVO>();
        if (status==0) {
            for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeqOrderBySiSeqDesc(uiSeq)) {
                GetBookListVO vo = new GetBookListVO(entity);
                list.add(vo);
            }
        }
        else if (status==4) {
            for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeqAndSiStatusOrderBySiSeqDesc(uiSeq, status)) {
                GetBookListVO vo = new GetBookListVO(entity);
                list.add(vo);
            }
        }
        else if (status==3) {
            for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeqAndSiStatusOrderBySiSeqDesc(uiSeq, status)) {
                GetBookListVO vo = new GetBookListVO(entity);
                list.add(vo);
            }
        }
        else if (status==1) {
            for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeqAndSiStatusOrderBySiSeqDesc(uiSeq, status)) {
                GetBookListVO vo = new GetBookListVO(entity);
                list.add(vo);
            }
        }
        return new GetMyBookVO(uiSeq, list);
    }

    public BasicResponse patchBookStatus(Long id) {
        if (scheduleInfoRepository.findById(id).isEmpty()) {
            throw new InvalidInputException("???????????? ?????? ???????????????.");
        }
        else if (scheduleInfoRepository.findById(id).get().getSiStatus()==4) {
            throw new InvalidInputException("?????? ????????? ????????????.");
        }
        else {
            ScheduleInfoEntity entity = scheduleInfoRepository.findById(id).get();
            UserInfoEntity uEntity = userInfoRepository.findById(entity.getSiUiSeq()).orElseThrow(() -> new InvalidInputException("???????????? ?????? ?????? ?????? ?????????."));
            BookInfoEntity bEntity = bookInfoRepository.findById(entity.getSiBiSeq()).orElseThrow(() -> new InvalidInputException("???????????? ?????? ??? ?????? ?????????."));
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
        return new BasicResponse("true", "???????????? ??????????????????.");
    }

    public BasicResponse deleteBook(Long id) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(id).orElseThrow(() -> new InvalidInputException("???????????? ?????? ???????????????."));
        UserInfoEntity uEntity = userInfoRepository.findById(entity.getSiUiSeq()).orElseThrow(() -> new InvalidInputException("???????????? ?????? ?????? ?????? ?????????."));
        BookInfoEntity bEntity = bookInfoRepository.findById(entity.getSiBiSeq()).orElseThrow(() -> new InvalidInputException("???????????? ?????? ??? ?????? ?????????."));
        Integer status = entity.getSiStatus();
        scheduleInfoRepository.delete(entity);
        if (status==4) {
            Integer page = bEntity.getBiPage();
            UserInfoEntity newEntity = new UserInfoEntity(uEntity, false, page);
            userInfoRepository.save(newEntity);
        }
        return new BasicResponse("true", "?????????????????????.");
    }
}
