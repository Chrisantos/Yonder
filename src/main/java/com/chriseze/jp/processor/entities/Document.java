package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT")
@NamedQuery(name = Document.FIND_BY_TALENT, query = "select d from Document d where d.talent.email = :param or d.talent.name = :param")
@NamedQuery(name = Document.FIND_BY_TITLE, query = "select d from Document d where d.title = :title")
public class Document extends AbstractBaseEntity {
    private static final long serialVersionUID = 1922620635724834931L;

    public static final String FIND_BY_TALENT = "Document.findByTalent";
    public static final String FIND_BY_TITLE = "Document.findByTitle";

    @Column(name = "TITLE", nullable = false)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "TALENT", nullable = false)
    private Talent talent;

    @Column(name = "RAW_DOCUMENT", nullable = false)
    private String document;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
