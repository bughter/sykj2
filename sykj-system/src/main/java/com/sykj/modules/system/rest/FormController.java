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
import com.sykj.modules.system.domain.Form;
import com.sykj.modules.system.service.FormService;
import com.sykj.modules.system.service.dto.FormQueryCriteria;
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
* @date 2021-05-11
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/form管理")
@RequestMapping("/api/form")
public class FormController {

    private final FormService formService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('form:list')")
    public void download(HttpServletResponse response, FormQueryCriteria criteria) throws IOException {
        formService.download(formService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/form")
    @ApiOperation("查询api/form")
    @PreAuthorize("@el.check('form:list')")
    public ResponseEntity<Object> query(FormQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(formService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/form")
    @ApiOperation("新增api/form")
    @PreAuthorize("@el.check('form:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Form resources){
        return new ResponseEntity<>(formService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/form")
    @ApiOperation("修改api/form")
    @PreAuthorize("@el.check('form:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Form resources){
        formService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/form")
    @ApiOperation("删除api/form")
    @PreAuthorize("@el.check('form:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        formService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}