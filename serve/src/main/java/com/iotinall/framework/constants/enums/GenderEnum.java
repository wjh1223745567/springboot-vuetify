package com.iotinall.framework.constants.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum GenderEnum {

    WOMAN(0, "女"),
    MAN(1, "男"),
    UNKNOWN(2, "未知");

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private final int code;

    private final String name;

    /**
     *
     * @param code
     * @return
     */
    public static GenderEnum findByCode(Integer code){
        if(code == null){
            return GenderEnum.UNKNOWN;
        }

        for (GenderEnum value : GenderEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        return GenderEnum.UNKNOWN;
    }

}
