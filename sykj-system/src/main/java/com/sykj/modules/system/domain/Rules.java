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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-04-20
**/
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@Table(name="rules")
public class Rules implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rules_id")
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "rules_name")
    @ApiModelProperty(value = "规则集名称")
    private String rulesName;

    @Column(name = "company")
    @ApiModelProperty(value = "所属公司")
    private Integer company;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "create_by")
    @CreatedBy
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by")
    @LastModifiedBy
    @ApiModelProperty(value = "最后更新")
    private String updateBy;

    @ManyToMany
    @ApiModelProperty(value = "规则集")
    @JoinTable(name = "rule_rules",
            joinColumns = {@JoinColumn(name = "rules_id",referencedColumnName = "rules_id")},
            inverseJoinColumns = {@JoinColumn(name = "rule_id",referencedColumnName = "rule_id")})
    private Set<Rule> ruleSet;

    public void copy(Rules source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}