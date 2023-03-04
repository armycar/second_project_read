package com.readers.be3.entity.image;

import org.hibernate.annotations.DynamicInsert;

import com.readers.be3.entity.UserInfoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "user_img")
public class UserImgEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uimg_seq") private Long uimgSeq;
    @Column(name = "uimg_filename") private String uimgFilename;
    @Column(name = "uimg_uri") private String uimgUri;
    @Column(name = "uimg_ui_seq") private Long uimgUiSeq;
 	@OneToOne
    // (cascade=CascadeType.ALL)
    @JoinColumn(name = "uimg_ui_seq", insertable = false, updatable = false) UserInfoEntity userInfoEntity;
}
