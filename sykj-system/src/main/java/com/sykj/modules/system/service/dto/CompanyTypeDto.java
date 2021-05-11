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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-04-28
**/
@Data
public class CompanyTypeDto implements Serializable {

    /** 主键 */
    private String companyTypeId;

    /** 名称 */
    private String name;

    /** 累计 */
    private Integer count;
}