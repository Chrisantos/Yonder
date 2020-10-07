package com.chriseze.jp.processor.restartifacts;

import com.chriseze.jp.processor.enums.ResponseEnum;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1956755339236651759L;

    private int code = -1;
    private String description;

    public BaseResponse() {}

    public BaseResponse(ResponseEnum responseEnum) {
        if (responseEnum != null) {
            this.code = responseEnum.getCode();
            this.description = responseEnum.getDescription();
        }
    }

    public void assignResponseEnum(ResponseEnum responseEnum) {
        if (responseEnum != null) {
            this.code = responseEnum.getCode();
            this.description = responseEnum.getDescription();
        }
    }
}
