package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.ScheduleInfoEntity;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfoEntity, Long> {

  ScheduleInfoEntity findBySiSeq(Long SiSeq);
    @Query(value = "select * from schedule_info where si_ui_seq = :siUiSeq and si_status = 3", nativeQuery = true)
    public List<ScheduleInfoEntity> findBySiUiSeq(@Param("siUiSeq") Long siUiSeq);

  List<ScheduleInfoEntity> findBySiUiSeqAndSiStatus(Long siUiSeq, Integer siStatus);
  // List<ScheduleInfoEntity> findBySiUiSeqAndSiStatusInOrderBySiSeqDesc(Long siUiSeq, List<Integer> statusList);
  List<ScheduleInfoEntity> findBySiUiSeqAndSiStatusOrderBySiSeqDesc(Long siUiSeq, Integer status);
  
  @Query(value = "select * from schedule_info where si_ui_seq = :siUiSeq order by si_seq desc", nativeQuery = true)
  public List<ScheduleInfoEntity> findBySiUiSeqOrderBySiSeqDesc(@Param("siUiSeq") Long siUiSeq);
  public ScheduleInfoEntity findBySiUiSeqAndSiBiSeq(Long siUiSeq, Long siBiSeq);
}
