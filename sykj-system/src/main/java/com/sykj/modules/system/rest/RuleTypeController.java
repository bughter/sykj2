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
import com.sykj.modules.system.domain.RuleType;
import com.sykj.modules.system.service.RuleTypeService;
import com.sykj.modules.system.service.dto.RuleTypeQueryCriteria;
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
* @date 2021-04-08
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/system/rule_type管理")
@RequestMapping("/api/ruleType")
public class RuleTypeController {

    private final RuleTypeService ruleTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ruleType:list')")
    public void download(HttpServletResponse response, RuleTypeQueryCriteria criteria) throws IOException {
        ruleTypeService.download(ruleTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/system/rule_type")
    @ApiOperation("查询api/system/rule_type")
    @PreAuthorize("@el.check('ruleType:list')")
    public ResponseEntity<Object> query(RuleTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ruleTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/system/rule_type")
    @ApiOperation("新增api/system/rule_type")
    @PreAuthorize("@el.check('ruleType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RuleType resources){
        return new ResponseEntity<>(ruleTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/system/rule_type")
    @ApiOperation("修改api/system/rule_type")
    @PreAuthorize("@el.check('ruleType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RuleType resources){
        ruleTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/system/rule_type")
    @ApiOperation("删除api/system/rule_type")
    @PreAuthorize("@el.check('ruleType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        ruleTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}