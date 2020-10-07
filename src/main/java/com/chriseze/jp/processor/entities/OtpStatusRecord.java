package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OTP_STATUS_RECORD")
@NamedQuery(name = OtpStatusRecord.FIND_BY_PHONE_AND_USED, query = "select o from OtpStatusRecord o where o.phoneNumber = :phoneNumber and o.otpUsed = :otpUsed")
public class OtpStatusRecord extends AbstractBaseEntity {

    private static final long serialVersionUID = -1577399024704787794L;

    public static final String FIND_BY_PHONE_AND_USED = "OtpStatusRecord.find";

    @Column(name="OTP", nullable = false)
    private String otp;

    @Column(name="PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name="EXPIRATION_TIME")
    private LocalDateTime expirationTime;

    @Column(name="OTP_USED")
    private boolean otpUsed;

    @Column(name="TIME_USED")
    private LocalDateTime timeUsed;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
