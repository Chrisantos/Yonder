package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROJECT_APPLICANT")
public class ProjectApplicant extends AbstractBaseEntity {

    private static final long serialVersionUID = 5934412667913323267L;

    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "PROJECT")
    private Project project;

    @OneToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "APPLICANT")
    private Talent applicant;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }

}
