package com.chriseze.jp.processor.restartifacts;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCompletionResponse implements Serializable {
    private static final long serialVersionUID = 2802557711785753286L;

    private String talentEmail;
    private String clientEmail;
    private String projectTitle;
    private Integer projectFee;
    private long noOfDaysTaken;
    private int noOfApplicants;
}
