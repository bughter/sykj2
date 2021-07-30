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
import com.sykj.modules.system.domain.Wallet;
import com.sykj.modules.system.service.WalletService;
import com.sykj.modules.system.service.dto.WalletQueryCriteria;
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
* @date 2021-06-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "钱包api管理")
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wallet:list')")
    public void download(HttpServletResponse response, WalletQueryCriteria criteria) throws IOException {
        walletService.download(walletService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询钱包api")
    @ApiOperation("查询钱包api")
    @PreAuthorize("@el.check('wallet:list')")
    public ResponseEntity<Object> query(WalletQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(walletService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增钱包api")
    @ApiOperation("新增钱包api")
    @PreAuthorize("@el.check('wallet:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Wallet resources){
        return new ResponseEntity<>(walletService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改钱包api")
    @ApiOperation("修改钱包api")
    @PreAuthorize("@el.check('wallet:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Wallet resources){
        walletService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除钱包api")
    @ApiOperation("删除钱包api")
    @PreAuthorize("@el.check('wallet:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        walletService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}