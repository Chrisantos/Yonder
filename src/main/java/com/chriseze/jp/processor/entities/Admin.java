package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import java.time.LocalDate;
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
@Table(name = "ADMIN")
@NamedQuery(name = Admin.FIND_BY_EMAIL, query = "select a from Admin a where a.email = :email")
public class Admin extends AbstractBaseEntity {
    private static final long serialVersionUID = 84716368928153268L;

    public static final String FIND_BY_EMAIL = "Admin.findByEmail";

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    private String salt;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
