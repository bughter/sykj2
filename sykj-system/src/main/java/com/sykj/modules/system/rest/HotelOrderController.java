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
package com.sykj.modules.system.rest;

import com.sykj.annotation.Log;
import com.sykj.modules.system.domain.HotelOrder;
import com.sykj.modules.system.service.HotelOrderService;
import com.sykj.modules.system.service.dto.HotelOrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author czy
* @date 2021-07-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "酒店订单管理")
@RequestMapping("/api/hotelOrder")
public class HotelOrderController {

    private final HotelOrderService hotelOrderService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('hotelOrder:list')")
    public void download(HttpServletResponse response, HotelOrderQueryCriteria criteria) throws IOException {
        hotelOrderService.download(hotelOrderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询酒店订单")
    @ApiOperation("查询酒店订单")
    @PreAuthorize("@el.check('hotelOrder:list')")
    public ResponseEntity<Object> query(HotelOrderQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(hotelOrderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增酒店订单")
    @ApiOperation("新增酒店订单")
    @PreAuthorize("@el.check('hotelOrder:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody HotelOrder resources){
        return new ResponseEntity<>(hotelOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改酒店订单")
    @ApiOperation("修改酒店订单")
    @PreAuthorize("@el.check('hotelOrder:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody HotelOrder resources){
        hotelOrderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除酒店订单")
    @ApiOperation("删除酒店订单")
    @PreAuthorize("@el.check('hotelOrder:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        hotelOrderService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}