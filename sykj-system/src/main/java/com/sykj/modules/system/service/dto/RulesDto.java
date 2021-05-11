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

import com.sykj.modules.system.domain.Rule;
import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-04-20
**/
@Data
public class RulesDto implements Serializable {

    /** 主键 */
    private Integer id;

    /** 规则集名称 */
    private String rulesName;

    /** 所属公司 */
    private Integer company;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 创建者 */
    private String createBy;

    /** 最后更新 */
    private String updateBy;

    /**
     * 规则集合
     */
    private Set<Rule> ruleSet;
}