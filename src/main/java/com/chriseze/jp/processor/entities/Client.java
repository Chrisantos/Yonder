package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import com.chriseze.yonder.utils.enums.Gender;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@Entity
@Table(name = "CLIENT")
@NamedQuery(name = Client.FIND_ALL, query = "select c from Client c order by c.name")
@NamedQuery(name = Client.FIND_BY_EMAIL_OR_NAME, query = "select c from Client c where c.email = :param or c.name = :param")
public class Client extends AbstractBaseEntity {
    private static final long serialVersionUID = -3849857556399893639L;

    public static final String FIND_ALL = "Client.findAll";
    public static final String FIND_BY_EMAIL_OR_NAME = "Client.find";

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "clients", cascade= CascadeType.ALL)
    private Set<Talent> talents;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "client", cascade= CascadeType.ALL)
    private Set<Project> projects;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "client", cascade= CascadeType.ALL)
    private Set<Message> messages;

    private String salt;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
