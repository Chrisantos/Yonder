package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SKILL")
@NamedQuery(name = Skill.FIND_ALL, query = "select s from Skill s where s.talent.email = :email")
public class Skill extends AbstractBaseEntity {

    private static final long serialVersionUID = -1336630322920824154L;

    public static final String FIND_ALL = "Skill.findAll";

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY,cascade= CascadeType.ALL)
    @JoinColumn(name = "TALENT", nullable = false)
    private Talent talent;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
