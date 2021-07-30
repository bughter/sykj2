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

import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import com.sykj.annotation.Log;
import com.sykj.modules.system.domain.Post;
import com.sykj.modules.system.domain.User;
import com.sykj.modules.system.service.PostService;
import com.sykj.modules.system.service.dto.PostQueryCriteria;
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
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author czy
* @date 2021-04-29
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/post管理")
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('post:list')")
    public void download(HttpServletResponse response, PostQueryCriteria criteria) throws IOException {
        postService.download(postService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/post")
    @ApiOperation("查询api/post")
    @PreAuthorize("@el.check('post:list')")
    public ResponseEntity<Object> query(PostQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(postService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/getPost")
    @Log("查询api/post")
    @ApiOperation("查询api/post")
    @PreAuthorize("@el.check('post:list')")
    public ResponseEntity<Object> getPost(PostQueryCriteria criteria, Pageable pageable, String companyId){
        criteria.setCompanyId(companyId);
        return new ResponseEntity<>(postService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/c_list")
    @Log("查询api/post")
    @ApiOperation("查询api/post")
    @PreAuthorize("@el.check('post:clist')")
    public ResponseEntity<Object> query2(PostQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(postService.queryAll2(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/post")
    @ApiOperation("新增api/post")
    @PreAuthorize("@el.check('post:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Post resources){
        return new ResponseEntity<>(postService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/post")
    @ApiOperation("修改api/post")
    @PreAuthorize("@el.check('post:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Post resources){
        if(null == resources.getPPost().getPostId() || resources.getPPost().getPostId().isEmpty()){
            resources.setPPost(null);
        }
        postService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/post")
    @ApiOperation("删除api/post")
    @PreAuthorize("@el.check('post:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        postService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}