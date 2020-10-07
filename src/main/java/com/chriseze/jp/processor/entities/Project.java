package com.chriseze.jp.processor.entities;

import com.chriseze.yonder.utils.base.AbstractBaseEntity;
import com.chriseze.yonder.utils.enums.Industry;
import com.chriseze.yonder.utils.enums.ProjectStatus;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@Entity
@Table(name = "PROJECT")
@NamedQuery(name = Project.FIND_ALL, query = "select p from Project p")
public class Project extends AbstractBaseEntity {
    private static final long serialVersionUID = -4555869774874265877L;

    public static final String FIND_ALL = "Project.findAll";

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "NO_OF_APPLICANTS")
    private Integer noOfApplicants;

    @Column(name = "LOCATION")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ProjectStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "INDUSTRY")
    private Industry industry;

    @Column(name = "FEE")
    private Integer fee;

    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "CLIENT")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "TALENT")
    private Talent implementedBy;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "project", cascade= CascadeType.ALL)
    private Set<ProjectReview> projectReviews;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "project", cascade= CascadeType.ALL)
    private Set<ProjectApplicant> projectApplicants;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Recommendation> recommendations;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @PrePersist
    public void setCreateDate() {
        this.setCreateDate(LocalDate.now());
    }
}
