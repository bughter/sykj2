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
import com.sykj.modules.system.domain.Department;
import com.sykj.modules.system.service.DepartmentService;
import com.sykj.modules.system.service.dto.DepartmentQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * @author czy
 * @website https://el-admin.vip
 * @date 2021-08-03
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "部门管理管理")
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('department:list')")
    public void download(HttpServletResponse response, DepartmentQueryCriteria criteria) throws IOException {
        departmentService.download(departmentService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询部门管理")
    @ApiOperation("查询部门管理")
    @PreAuthorize("@el.check('department:list')")
    public ResponseEntity<Object> query(DepartmentQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(departmentService.queryAll(criteria, pageable), HttpStatus.OK);
    }
    @GetMapping(value = "/getPost")
    @Log("查询本公司部门")
    @ApiOperation("查询本公司部门")
    @PreAuthorize("@el.check('department:list')")
    public ResponseEntity<Object> getPost(DepartmentQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(departmentService.getDepts(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增部门管理")
    @ApiOperation("新增部门管理")
    @PreAuthorize("@el.check('department:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Department resources) {
        return new ResponseEntity<>(departmentService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改部门管理")
    @ApiOperation("修改部门管理")
    @PreAuthorize("@el.check('department:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Department resources) {
        departmentService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门管理")
    @ApiOperation("删除部门管理")
    @PreAuthorize("@el.check('department:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        departmentService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}