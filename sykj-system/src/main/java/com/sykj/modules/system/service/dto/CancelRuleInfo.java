package com.sykj.modules.system.service.dto;

import lombok.Data;

@Data
public class CancelRuleInfo {
    /**
     *	取消名称
     */
    private String name;
    /**
     *取消政策类型(1不可取销,2限时取消,3收费取消)
     */
    private int type;
    /**
     *	取消政策描述
     */
    private String desc;
}
