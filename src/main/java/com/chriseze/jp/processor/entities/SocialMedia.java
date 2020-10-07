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
@Table(name = "SOCIAL_MEDIA")
@NamedQuery(name = SocialMedia.FIND_ALL, query = "select s from SocialMedia s where s.talent.email = :email")
public class SocialMedia extends AbstractBaseEntity {
    private static final long serialVersionUID = 6546375706294885452L;

    public static final String FIND_ALL = "SocialMedia.findAll";

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "HANDLE")
    private String handle;

    @ManyToOne(fetch = FetchType.LAZY,cascade= CascadeType.ALL)
    @JoinColumn(name = "TALENT", nullable = false)
    private Talent talent;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
