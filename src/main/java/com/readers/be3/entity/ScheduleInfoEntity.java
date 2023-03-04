package com.readers.be3.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "schedule_info")
public class ScheduleInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "si_seq") private Long siSeq;
    @Column(name = "si_content") private String siContent;
    @Column(name = "si_start_date") private LocalDate siStartDate;
    @Column(name = "si_end_date") private LocalDate siEndDate;
    @Column(name = "si_status") private Integer siStatus;
    @Column(name = "si_ui_seq") private Long siUiSeq;
    @Column(name = "si_bi_seq") private Long siBiSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "si_ui_seq", insertable = false, updatable = false) private UserInfoEntity userInfoEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "si_bi_seq", insertable = false, updatable = false) private BookInfoEntity bookInfoEntity;
}
