package com.chriseze.jp.processor.restartifacts;

import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationPojo implements Serializable {
    private static final long serialVersionUID = 3425575362181877440L;

    private String talentEmail;
    private String talentFullName;
    private String talentPhoneNumber;
    private Set<String> skills;
    private Set<SocialMediaPojo> socialMediaPojos;
}
