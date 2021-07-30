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
import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author czy
* @date 2021-06-22
**/
@Data
public class WalletDetailDto implements Serializable {

    private Integer wdId;

    private Company company;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String createBy;

    private String updateBy;

    /** 转账金额 */
    private BigDecimal amount;

    /** 余额 */
    private BigDecimal balance;

    /** 充值类型 1.线下转账*/
    private Integer type;

    /** 凭证 */
    private String receipt;
}