package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REVIEW")
public class UserReview extends AbstractBaseEntity {
    private static final long serialVersionUID = 7848759048438607041L;

    @Column(name = "REVIEW", nullable = false)
    private String review;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLIENT")
    private Client client;

    @OneToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "TALENT")
    private Talent talent;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
