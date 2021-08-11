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

import com.sykj.annotation.AnonymousAccess;
import com.sykj.annotation.Log;
import com.sykj.modules.system.domain.ContactUs;
import com.sykj.modules.system.service.ContactUsService;
import com.sykj.modules.system.service.dto.ContactUsQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author czy
* @date 2021-08-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "联系我们管理")
@RequestMapping("/api/contactUs")
public class ContactUsController {

    private final ContactUsService contactUsService;
    private final String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('contactUs:list')")
    public void download(HttpServletResponse response, ContactUsQueryCriteria criteria) throws IOException {
        contactUsService.download(contactUsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询联系我们")
    @ApiOperation("查询联系我们")
    @PreAuthorize("@el.check('contactUs:list')")
    public ResponseEntity<Object> query(ContactUsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(contactUsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增联系我们")
    @ApiOperation("新增联系我们")
    @PreAuthorize("@el.check('contactUs:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ContactUs resources){
        return new ResponseEntity<>(contactUsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改联系我们")
    @ApiOperation("修改联系我们")
    @PreAuthorize("@el.check('contactUs:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ContactUs resources){
        contactUsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除联系我们")
    @ApiOperation("删除联系我们")
    @PreAuthorize("@el.check('contactUs:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        contactUsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "contact")
    @AnonymousAccess
    public String contactUs(String name,String email,String mobile,String message){
        ContactUs contactUs=new ContactUs();
        if(name.isEmpty()){
            return "姓名不能为空！";
        }
        if(email.isEmpty()){
            return "邮箱不能为空！";
        }
        if(mobile.isEmpty()){
            return "联系方式不能为空！";
        }else if(mobile.length() != 11){
            return "手机号应为11位数";
        } else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
            if(!isMatch){
                return "请输入正确的手机格式";
            }
        }
        if(null==contactUsService.findByPhone(mobile)){
            return "请不要重复提交！";
        }
        contactUs.setName(name);
        contactUs.setPhone(mobile);
        contactUs.setEmail(email);
        contactUs.setMessage(message);
        contactUs.setStatus(1);
        contactUsService.create(contactUs);
        return "OK";
    }

}