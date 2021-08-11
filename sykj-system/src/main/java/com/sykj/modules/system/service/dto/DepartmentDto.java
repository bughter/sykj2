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

import com.sykj.modules.system.domain.Company;
import com.sykj.modules.system.domain.Department;
import com.sykj.modules.system.domain.User;
import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-08-03
**/
@Data
public class DepartmentDto implements Serializable {

    /** 主键 */
    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /** 部门名称 */
    private String name;

    /** 上级部门 */
    private Department pDept;

    /** 创建人 */
    private String createBy;

    /** 最后更新 */
    private String updateBy;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 公司 */
    private Company company;

    /** 负责人 */
    private User manager;
}