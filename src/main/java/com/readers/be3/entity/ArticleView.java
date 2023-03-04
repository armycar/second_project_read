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
@Table(name="article_view")
public class ArticleView {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="bi_seq") private Long biSeq;
  @Column(name="ui_seq") private Long uiSeq;
  @Column(name="ai_title") private String aiTitle;
  @Column(name="ai_content") private String aiContent;
  @Column(name="ai_reg_dt") private LocalDateTime aiRegdt;
}
