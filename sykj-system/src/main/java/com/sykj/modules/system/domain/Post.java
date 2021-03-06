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
import javax.persistence.CascadeType;
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
* @date 2021-04-29
**/
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@Table(name="post")
public class Post implements Serializable {

    @Id
    @Column(name = "post_id")
    @ApiModelProperty(value = "主键")
    private String postId;

    @Column(name = "post_name")
    @ApiModelProperty(value = "职位名称")
    private String postName;

    @JoinColumn(name = "dept_id")
    @ManyToOne(fetch=FetchType.LAZY)
    @ApiModelProperty(value = "所属部门",hidden = true)
    private Department dept;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch=FetchType.LAZY)
    @ApiModelProperty(value = "所属公司",hidden = true)
    private Company company;

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

    public void copy(Post source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}