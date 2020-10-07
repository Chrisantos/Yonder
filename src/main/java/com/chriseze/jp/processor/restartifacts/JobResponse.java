package com.chriseze.jp.processor.restartifacts;

import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponse implements Serializable {

    private static final long serialVersionUID = 6936189715673026808L;

    private String title;
    private String description;
    private String location;
    private String postedBy;
    private Set<String> appliedBy;
    private Set<String> recommendations;
}
