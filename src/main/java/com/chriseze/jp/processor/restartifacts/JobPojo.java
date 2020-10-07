package com.chriseze.jp.processor.restartifacts;

import com.chriseze.yonder.utils.beanvalidation.ValidEnumString;
import com.chriseze.yonder.utils.enums.Industry;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class JobPojo implements Serializable {
    private static final long serialVersionUID = 2867935216787430705L;

    @NotBlank(message = "Please provide job title")
    private String title;

    @NotBlank(message = "Please provide job description")
    private String description;

    @NotBlank(message = "Please provide location of the job")
    private String location;

    @ValidEnumString(enumClass = Industry.class, message = "Industry type not valid")
    @NotBlank(message = "Industry cannot be blank")
    private String industry;

    @NotBlank(message = "Email of the job poster is required")
    private String postedBy;

    private Integer fee;
}
