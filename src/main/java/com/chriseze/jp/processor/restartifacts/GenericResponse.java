package com.chriseze.jp.processor.restartifacts;

import com.chriseze.jp.processor.enums.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<T> extends BaseResponse {
    private static final long serialVersionUID = -8955846164291006808L;

    private T result;

    public GenericResponse() {}

    public GenericResponse(ResponseEnum responseEnum) {
        if (responseEnum != null) {
            setCode(responseEnum.getCode());
            setDescription(responseEnum.getDescription());
        }
    }

    public void assignResponseEnum(ResponseEnum responseEnum) {
        if (responseEnum != null) {
            setCode(responseEnum.getCode());
            setDescription(responseEnum.getDescription());
        }
    }
}
