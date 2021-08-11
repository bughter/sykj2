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

import com.sykj.base.BaseEntity;
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

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-08-03
**/
@Entity
@Data
@Table(name="department")
@EntityListeners(AuditingEntityListener.class)
public class Department implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @ApiModelProperty(value = "部门名称")
    private String name;

    @JoinColumn(name = "p_id")
    @ManyToOne
    @ApiModelProperty(value = "上级部门",hidden = true)
    private Department pDept;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建人")
    @CreatedBy
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "最后更新")
    @LastModifiedBy
    private String updateBy;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;


    @JoinColumn(name = "company_id")
    @ManyToOne
    @ApiModelProperty(value = "公司",hidden = true)
    private Company company;


    @JoinColumn(name = "manager")
    @ManyToOne
    @ApiModelProperty(value = "负责人",hidden = true)
    private User manager;

    public void copy(Department source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}