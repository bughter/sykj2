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
import com.sykj.modules.system.domain.CompanyType;
import com.sykj.modules.system.service.CompanyTypeService;
import com.sykj.modules.system.service.dto.CompanyTypeQueryCriteria;
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
* @date 2021-04-28
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/company_type管理")
@RequestMapping("/api/companyType")
public class CompanyTypeController {

    private final CompanyTypeService companyTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('companyType:list')")
    public void download(HttpServletResponse response, CompanyTypeQueryCriteria criteria) throws IOException {
        companyTypeService.download(companyTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/company_type")
    @ApiOperation("查询api/company_type")
    @PreAuthorize("@el.check('companyType:list')")
    public ResponseEntity<Object> query(CompanyTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/company_type")
    @ApiOperation("新增api/company_type")
    @PreAuthorize("@el.check('companyType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyType resources){
        return new ResponseEntity<>(companyTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/company_type")
    @ApiOperation("修改api/company_type")
    @PreAuthorize("@el.check('companyType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyType resources){
        companyTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/company_type")
    @ApiOperation("删除api/company_type")
    @PreAuthorize("@el.check('companyType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        companyTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}