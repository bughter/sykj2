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
import com.sykj.modules.system.domain.Rules;
import com.sykj.modules.system.service.RulesService;
import com.sykj.modules.system.service.dto.RulesQueryCriteria;
import com.sykj.utils.UUIDUtils;
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
* @date 2021-04-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/rules管理")
@RequestMapping("/api/rules")
public class RulesController {

    private final RulesService rulesService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rules:list')")
    public void download(HttpServletResponse response, RulesQueryCriteria criteria) throws IOException {
        rulesService.download(rulesService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/rules")
    @ApiOperation("查询api/rules")
    @PreAuthorize("@el.check('rules:list')")
    public ResponseEntity<Object> query(RulesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rulesService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/rules")
    @ApiOperation("新增api/rules")
    @PreAuthorize("@el.check('rules:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Rules resources){
        return new ResponseEntity<>(rulesService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/rules")
    @ApiOperation("修改api/rules")
    @PreAuthorize("@el.check('rules:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Rules resources){
        rulesService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/rules")
    @ApiOperation("删除api/rules")
    @PreAuthorize("@el.check('rules:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        rulesService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}