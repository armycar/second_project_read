package com.readers.be3.entity;

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
@Table(name="my_rank")
public class MyPageView {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column (name="ranking") private Long rankSeq;
  @Column(name="ui_seq") private Long uiSeq;
  @Column(name="ui_nickname") private String uiNickName;
  @Column(name="ui_point") private Integer uiPoint;
  @Column(name="ui_total_page") private Integer uiTotalPage;
  @Column(name="ui_total_book") private Integer uiTotalBook;
  @Column(name="uimg_uri") private String uimgUri;
}
