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
package com.sykj.modules.system.repository;

import com.sykj.modules.system.domain.HotelOrder;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @website https://el-admin.vip
* @author czy
* @date 2021-07-19
**/
public interface HotelOrderRepository extends JpaRepository<HotelOrder, String>, JpaSpecificationExecutor<HotelOrder> {
    HotelOrder findHotelOrderByOrderNo(String orderNo);
}