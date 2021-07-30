package com.sykj.modules.system.service.dto;

import lombok.Data;

@Data
public class RatePlanInfo {
    /**
     *产品（价格计划）ID
     */
    private String ratePlanId;
    /**
     *产品名称
     */
    private String ratePlanName;
    /**
     *支付方式 0：预付，1：现付
     */
    private String paymentType;
    /**
     *平均价格
     */
    private String averagePrice;
    /**
     *每日价格，以“|”分隔，数量需与入住/离店时间对应，如2019-05-20入住，2019-05-22离店，300|300指第1天300元，第2天300元
     */
    private String pricePerDay;
    /**
     *库存量（提供参考），房间每天的库存量，与销售指导价对应；以“|”分隔，如2019-05-20入住，2019-05-22离店，3|5指第1天剩3间房，第2天剩5间房
     */
    private String stockPerDay;
    /**
     *确认类型：0 不支持即时确认 1 支持即时确认
     */
    private String confirmType;
    /**
     *	早餐规则,值如：含有2份免费早餐 | 不含早餐
     */
    private String breakfast;
    /**
     * 取消规则
     */
    private CancelRuleInfo cancelRule;
}
