package com.readers.be3.entity.image;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name = "article_img")
public class ArticleImgEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aimg_seq") private Long aimgSeq;
    @Column(name = "aimg_filename") private String aimgFilename;
    @Column(name = "aimg_uri") private String aimgUri;
    @Column(name = "aimg_order") private Integer aimgOrder;
    @Column(name = "aimg_ai_seq") private Long aimgAiSeq;
     // @ManyToOne @JoinColumn(name = "aimg_ai_seq")  ArticleInfoEntity article;
}
