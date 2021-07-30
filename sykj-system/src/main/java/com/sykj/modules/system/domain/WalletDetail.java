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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-06-22
**/
@Entity
@Data
@Table(name="wallet_detail")
@EntityListeners(AuditingEntityListener.class)
public class WalletDetail implements Serializable {

    @Id
    @Column(name = "wd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "wdId")
    private Integer wdId;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch=FetchType.LAZY)
    @ApiModelProperty(value = "公司", hidden = true)
    private Company company;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "create_by")
    @CreatedBy
    @ApiModelProperty(value = "createBy")
    private String createBy;

    @Column(name = "update_by")
    @LastModifiedBy
    @ApiModelProperty(value = "updateBy")
    private String updateBy;

    @Column(name = "amount")
    @ApiModelProperty(value = "转账金额")
    private BigDecimal amount;

    @Column(name = "balance")
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @Column(name = "type")
    @ApiModelProperty(value = "type")
    private Integer type;

    @Column(name = "receipt")
    @ApiModelProperty(value = "凭证")
    private String receipt;

    public void copy(WalletDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}