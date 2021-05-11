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
import com.sykj.modules.system.domain.City;
import com.sykj.modules.system.service.CityService;
import com.sykj.modules.system.service.dto.CityQueryCriteria;
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
* @date 2021-04-07
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/city管理")
@RequestMapping("/api/city")
public class CityController {

    private final CityService cityService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('city:list')")
    public void download(HttpServletResponse response, CityQueryCriteria criteria) throws IOException {
        cityService.download(cityService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/city")
    @ApiOperation("查询api/city")
    @PreAuthorize("@el.check('city:list')")
    public ResponseEntity<Object> query(CityQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(cityService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/city")
    @ApiOperation("新增api/city")
    @PreAuthorize("@el.check('city:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody City resources){
        return new ResponseEntity<>(cityService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/city")
    @ApiOperation("修改api/city")
    @PreAuthorize("@el.check('city:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody City resources){
        cityService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/city")
    @ApiOperation("删除api/city")
    @PreAuthorize("@el.check('city:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        cityService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}