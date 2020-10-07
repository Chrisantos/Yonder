package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import com.chriseze.yonder.utils.enums.Gender;
import com.chriseze.yonder.utils.enums.Industry;
import com.chriseze.yonder.utils.enums.Level;
import com.chriseze.yonder.utils.enums.StatusEnum;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "TALENT")
@NamedQuery(name = Talent.FIND_ALL, query = "select t from Talent t order by t.name")
@NamedQuery(name = Talent.FIND_BY_EMAIL_OR_NAME, query = "select t from Talent t where t.email = :param or t.name = :param")
public class Talent extends AbstractBaseEntity {

    private static final long serialVersionUID = 3725063607283715266L;

    public static final String FIND_ALL = "Talent.findAll";
    public static final String FIND_BY_EMAIL_OR_NAME = "Talent.find";

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private StatusEnum status;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "RATINGS")
    private Double ratings;

    @Column(name = "HOURLY_RATE", nullable = false)
    private Integer hourlyRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "INDUSTRY")
    private Industry industry;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<Skill> skills;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<SocialMedia> socialMedia;

    @ManyToMany(cascade= CascadeType.ALL)
    @JoinTable(name = "TALENT_CLIENT", joinColumns = {@JoinColumn(name = "TALENT_ID")}, inverseJoinColumns = {@JoinColumn(name = "CLIENT_ID")})
    private Set<Client> clients;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<UserReview> userReviews;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<Message> messages;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<Document> documents;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "implementedBy", cascade= CascadeType.ALL)
    private Set<Project> projects;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "talent", cascade= CascadeType.ALL)
    private Set<Recommendation> recommendations;

    @Enumerated(EnumType.STRING)
    @Column(name = "Level")
    private Level level;

    private String salt;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
