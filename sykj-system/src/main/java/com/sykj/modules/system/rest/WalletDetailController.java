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
import com.sykj.modules.system.domain.WalletDetail;
import com.sykj.modules.system.service.WalletDetailService;
import com.sykj.modules.system.service.dto.WalletDetailQueryCriteria;
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
* @date 2021-06-22
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "钱包明细管理")
@RequestMapping("/api/walletDetail")
public class WalletDetailController {

    private final WalletDetailService walletDetailService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('walletDetail:list')")
    public void download(HttpServletResponse response, WalletDetailQueryCriteria criteria) throws IOException {
        walletDetailService.download(walletDetailService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询钱包明细")
    @ApiOperation("查询钱包明细")
    @PreAuthorize("@el.check('walletDetail:list')")
    public ResponseEntity<Object> query(WalletDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(walletDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value="/wallet_detail_list")
    @Log("查询钱包明细")
    @ApiOperation("查询钱包明细")
    @PreAuthorize("@el.check('walletDetail:list')")
    public ResponseEntity<Object> query2(WalletDetailQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(walletDetailService.queryAll2(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增钱包明细")
    @ApiOperation("新增钱包明细")
    @PreAuthorize("@el.check('walletDetail:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody WalletDetail resources){
        return new ResponseEntity<>(walletDetailService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改钱包明细")
    @ApiOperation("修改钱包明细")
    @PreAuthorize("@el.check('walletDetail:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody WalletDetail resources){
        walletDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除钱包明细")
    @ApiOperation("删除钱包明细")
    @PreAuthorize("@el.check('walletDetail:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        walletDetailService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}