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
package com.sykj.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-07-19
**/
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@Table(name="hotel_order")
public class HotelOrder implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "remark")
    @ApiModelProperty(value = "订单用户备注")
    private String remark;

    @Column(name = "order_no")
    @ApiModelProperty(value = "酒店订单号")
    private String orderNo;

    @Column(name = "number_of_rooms")
    @ApiModelProperty(value = "预订房间数量")
    private Integer numberOfRooms;

    @Column(name = "number_of_nights")
    @ApiModelProperty(value = "间夜数量")
    private Integer numberOfNights;

    @Column(name = "arrive_time")
    @ApiModelProperty(value = "最晚到底时间")
    private String arriveTime;

    @Column(name = "check_in_date")
    @ApiModelProperty(value = "入住日期")
    private String checkInDate;

    @Column(name = "check_out_date")
    @ApiModelProperty(value = "离店日期")
    private String checkOutDate;

    @Column(name = "guest_names")
    @ApiModelProperty(value = "入住人姓名，多个入住人姓名之间用‘|’分割")
    private String guestNames;

    @Column(name = "order_amount")
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;

    @Column(name = "real_amount")
    @ApiModelProperty(value = "真实金额（加价后金额）")
    private BigDecimal realAmount;

    @Column(name = "order_status")
    @ApiModelProperty(value = "订单状态（1=待付款，2=待确认，3=已确认，4=确认失败，5=已完成，6=取消中，7=取消失败，8=已取消）")
    private Integer orderStatus;

    @Column(name = "pay_status")
    @ApiModelProperty(value = "支付状态（0=待支付，1=已支付）")
    private Integer payStatus;

    @Column(name = "pay_time")
    @ApiModelProperty(value = "支付时间(支付成功才有值，格式：yyyy-MM-dd HH:mm:ss)")
    private Timestamp payTime;

    @Column(name = "payment_type")
    @ApiModelProperty(value = "支付方式(0未支付 1虚拟钱包支付 2支付宝 3微信 暂时只支持虚拟钱包支付)")
    private Integer paymentType;

    @Column(name = "transaction_no")
    @ApiModelProperty(value = "交易单号")
    private String transactionNo;

    @Column(name = "refund_amount")
    @ApiModelProperty(value = "订房失败或取消成功，退款金额")
    private BigDecimal refundAmount;

    @Column(name = "refund_success_time")
    @ApiModelProperty(value = "订房失败或取消成功需退款，退款成功时间")
    private Timestamp refundSuccessTime;

    @Column(name = "refund_transaction_no")
    @ApiModelProperty(value = "退款交易单号")
    private String refundTransactionNo;

    @Column(name = "refund_transaction_methods")
    @ApiModelProperty(value = "退款交易方式 (对应枚举TransactionMethods值:0未支付 1钱包支付 2支付宝 3微信)")
    private String refundTransactionMethods;

    @Column(name = "hotel_id")
    @ApiModelProperty(value = "酒店ID")
    private Integer hotelId;

    @Column(name = "hotel_name")
    @ApiModelProperty(value = "酒店名称")
    private String hotelName;

    @Column(name = "hotel_address")
    @ApiModelProperty(value = "酒店地址")
    private String hotelAddress;

    @Column(name = "room_type_name")
    @ApiModelProperty(value = "房型名称")
    private String roomTypeName;

    @Column(name = "bed_type_name")
    @ApiModelProperty(value = "床型")
    private String bedTypeName;

    @Column(name = "rate_plan_id")
    @ApiModelProperty(value = "产品(价格计划)ID")
    private String ratePlanId;

    @Column(name = "price_per_day")
    @ApiModelProperty(value = "每日价格，以“|”分隔，数量需与入住/离店时间对应，如2019-05-20入住，2019-05-22离店，300|200指第1天300元，第2天200元")
    private String pricePerDay;

    @Column(name = "breakfast_desc")
    @ApiModelProperty(value = "早餐规则描述")
    private String breakfastDesc;

    @Column(name = "cancel_policy_type")
    @ApiModelProperty(value = "取消政策类型(1不可取销,2限时取消,3收费取消)")
    private Integer cancelPolicyType;

    @Column(name = "cancel_desc")
    @ApiModelProperty(value = "取消政策描述")
    private String cancelDesc;

    @Column(name = "confirmation_number")
    @ApiModelProperty(value = "酒店确认号(可能为空，因为有的酒店不需要确认号)")
    private String confirmationNumber;

    @Column(name = "contact_name")
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @Column(name = "contact_mobile")
    @ApiModelProperty(value = "联系人手机号")
    private String contactMobile;

    @Column(name = "contact_email")
    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @Column(name = "pay_limit_time")
    @ApiModelProperty(value = "支付时限参考时间")
    private Timestamp payLimitTime;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "下单时间")
    private Timestamp createTime;

    public void copy(HotelOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}