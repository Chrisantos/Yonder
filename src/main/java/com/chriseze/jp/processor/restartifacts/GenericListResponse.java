package com.chriseze.jp.processor.restartifacts;

import com.chriseze.jp.processor.enums.ResponseEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericListResponse<T> extends BaseResponse {
    private static final long serialVersionUID = 647507996959620867L;

    private List<T> results = new ArrayList<>();

    public GenericListResponse() {}

    public GenericListResponse(ResponseEnum responseEnum) {
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
