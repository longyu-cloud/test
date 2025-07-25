package cn.longyu.conmon.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonStatus implements BaseEnum{
    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    ;
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    public static CommonStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (CommonStatus commonStatus : values()) {
            if (commonStatus.getValue() == value) {
                return commonStatus;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        CommonStatus status = of(value);
        return status.getDesc();
    }
}