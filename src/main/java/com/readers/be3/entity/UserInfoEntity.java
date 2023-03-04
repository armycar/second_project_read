package com.readers.be3.entity;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;

import com.readers.be3.entity.image.UserImgEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user_info")
public class UserInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ui_seq") private Long uiSeq;
    @Column(name = "ui_email") private String uiEmail;
    @Column(name = "ui_pwd") private String uiPwd;
    @Column(name = "ui_nickname") private String uiNickname;
    @Column(name = "ui_reg_dt") private Date uiRegDt;
    @Column(name = "ui_point") private Integer uiPoint;
    @Column(name = "ui_total_page") private Integer uiTotalPage;
    @Column(name = "ui_total_book") private Integer uiTotalBook;
    @Column(name = "ui_uid") private String uiUid;
    @Column(name = "ui_login_type") private String uiLoginType;
    @Column(name = "ui_status") private Integer uiStatus;
    @Column(name = "ui_leave_dt") private Date uiLeaveDt;
    @OneToOne(mappedBy = "userInfoEntity") private UserImgEntity userImgEntity;

    public static UserInfoEntity ofSNS(String uiUid, String uiLoginType){
        return new UserInfoEntity(null, null, null, "user#"+Calendar.getInstance().getTimeInMillis(), null, null, null, null, uiUid, uiLoginType, null, null, null);
    }

    // 도서 등록여부(true/false)에 따른 완독서 수/페이지수/포인트 업데이트
    public UserInfoEntity(UserInfoEntity entity, Boolean book, Integer page) {
        this.uiSeq = entity.getUiSeq();
        this.uiEmail = entity.getUiEmail();
        this.uiPwd = entity.getUiPwd();
        this.uiNickname = entity.getUiNickname();
        this.uiRegDt = entity.getUiRegDt();
        if (book) {
            this.uiPoint = entity.getUiPoint() + page;
            this.uiTotalBook = entity.getUiTotalBook() + 1;
            this.uiTotalPage = entity.getUiTotalPage() + page;
        }
        else {
            this.uiPoint = entity.getUiPoint() - page;
            this.uiTotalBook = entity.getUiTotalBook() - 1;
            this.uiTotalPage = entity.getUiTotalPage() - page;
        }
        this.uiUid = entity.getUiUid();
        this.uiLoginType = entity.getUiLoginType();
        this.uiStatus = entity.getUiStatus();
        this.uiLeaveDt = entity.getUiLeaveDt();
    }

    // 한줄평 & 독후감 작성에 따른 포인트 업데이트
    public UserInfoEntity(UserInfoEntity entity, Integer point) {
        this.uiSeq = entity.getUiSeq();
        this.uiEmail = entity.getUiEmail();
        this.uiPwd = entity.getUiPwd();
        this.uiNickname = entity.getUiNickname();
        this.uiRegDt = entity.getUiRegDt();
        this.uiPoint = entity.getUiPoint() + point;
        this.uiTotalBook = entity.getUiTotalBook();
        this.uiTotalPage = entity.getUiTotalPage();
        this.uiUid = entity.getUiUid();
        this.uiLoginType = entity.getUiLoginType();
        this.uiStatus = entity.getUiStatus();
        this.uiLeaveDt = entity.getUiLeaveDt();
    }
}
