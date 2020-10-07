package com.chriseze.jp.processor.restartifacts;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HireTalentPojo implements Serializable {

    private static final long serialVersionUID = 3790225770414310195L;

    private String talentEmail;
    private String clientEmail;
    private String projectTitle;

}
