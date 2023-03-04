package com.readers.be3.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name="one_comment_view")
public class OneCommentView {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="bi_seq") private Long biSeq;
  @Column(name="ui_seq") private Long uiSeq;
  @Column(name="oc_comment") private String ocComment;
  @Column(name="oc_score") private Integer ocScore;
  @Column(name="oc_reg_dt") private LocalDateTime ocRegdt;
}
