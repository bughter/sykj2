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
import com.sykj.modules.system.service.RuleService;
import com.sykj.modules.system.service.dto.RuleQueryCriteria;
import com.sykj.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.sykj.modules.system.domain.Rule;

/**
* @website https://el-admin.vip
* @author czy
* @date 2021-04-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/rule管理")
@RequestMapping("/api/rule")
public class RuleController {

    private final RuleService ruleService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rule:list')")
    public void download(HttpServletResponse response, RuleQueryCriteria criteria) throws IOException {
        ruleService.download(ruleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/rule")
    @ApiOperation("查询api/rule")
    @PreAuthorize("@el.check('rule:list')")
    public ResponseEntity<Object> query(RuleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ruleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/rule")
    @ApiOperation("新增api/rule")
    @PreAuthorize("@el.check('rule:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Rule resources){
        return new ResponseEntity<>(ruleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/rule")
    @ApiOperation("修改api/rule")
    @PreAuthorize("@el.check('rule:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Rule resources){
        resources.setUpdateBy(SecurityUtils.getCurrentUser().getUsername());
        ruleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/rule")
    @ApiOperation("删除api/rule")
    @PreAuthorize("@el.check('rule:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        ruleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}