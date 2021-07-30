/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.sykj.modules.system.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-07-19
**/
@Data
public class HotelOrderDto implements Serializable {

    /** 主键 */
    private String id;

    /** 订单用户备注 */
    private String remark;

    /** 酒店订单号 */
    private String orderNo;

    /** 预订房间数量 */
    private Integer numberOfRooms;

    /** 间夜数量 */
    private Integer numberOfNights;

    /** 最晚到底时间 */
    private String arriveTime;

    /** 入住日期 */
    private String checkInDate;

    /** 离店日期 */
    private String checkOutDate;

    /** 入住人姓名，多个入住人姓名之间用‘|’分割 */
    private String guestNames;

    /** 订单总金额 */
    private BigDecimal orderAmount;

    /** 订单总金额 */
    private BigDecimal realAmount;

    /** 订单状态（1=待付款，2=待确认，3=已确认，4=确认失败，5=已完成，6=取消中，7=取消失败，8=已取消） */
    private Integer orderStatus;

    /** 支付状态（0=待支付，1=已支付） */
    private Integer payStatus;

    /** 支付时间(支付成功才有值，格式：yyyy-MM-dd HH:mm:ss) */
    private Timestamp payTime;

    /** 支付方式(0未支付 1虚拟钱包支付 2支付宝 3微信 暂时只支持虚拟钱包支付) */
    private Integer paymentType;

    /** 交易单号 */
    private String transactionNo;

    /** 订房失败或取消成功，退款金额 */
    private BigDecimal refundAmount;

    /** 订房失败或取消成功需退款，退款成功时间 */
    private Timestamp refundSuccessTime;

    /** 退款交易单号 */
    private String refundTransactionNo;

    /** 退款交易方式 (对应枚举TransactionMethods值:0未支付 1钱包支付 2支付宝 3微信) */
    private String refundTransactionMethods;

    /** 酒店ID */
    private Integer hotelId;

    /** 酒店名称 */
    private String hotelName;

    /** 酒店地址 */
    private String hotelAddress;

    /** 房型名称 */
    private String roomTypeName;

    /** 床型 */
    private String bedTypeName;

    /** 产品(价格计划)ID */
    private String ratePlanId;

    /** 每日价格，以“|”分隔，数量需与入住/离店时间对应，如2019-05-20入住，2019-05-22离店，300|200指第1天300元，第2天200元 */
    private String pricePerDay;

    /** 早餐规则描述 */
    private String breakfastDesc;

    /** 取消政策类型(1不可取销,2限时取消,3收费取消) */
    private Integer cancelPolicyType;

    /** 取消政策描述 */
    private String cancelDesc;

    /** 酒店确认号(可能为空，因为有的酒店不需要确认号) */
    private String confirmationNumber;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机号 */
    private String contactMobile;

    /** 联系人邮箱 */
    private String contactEmail;

    /** 支付时限参考时间 */
    private Timestamp payLimitTime;

    /** 下单时间 */
    private Timestamp createTime;
}