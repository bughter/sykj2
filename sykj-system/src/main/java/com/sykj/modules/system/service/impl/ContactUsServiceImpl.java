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
package com.sykj.modules.system.service.impl;

import com.sykj.modules.system.domain.ContactUs;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.ContactUsRepository;
import com.sykj.modules.system.service.ContactUsService;
import com.sykj.modules.system.service.dto.ContactUsDto;
import com.sykj.modules.system.service.dto.ContactUsQueryCriteria;
import com.sykj.modules.system.service.mapstruct.ContactUsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sykj.utils.PageUtil;
import com.sykj.utils.QueryHelp;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author czy
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-08-02
 **/
@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;
    private final ContactUsMapper contactUsMapper;

    @Override
    public Map<String, Object> queryAll(ContactUsQueryCriteria criteria, Pageable pageable) {
        Page<ContactUs> page = contactUsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(contactUsMapper::toDto));
    }

    @Override
    public List<ContactUsDto> queryAll(ContactUsQueryCriteria criteria) {
        return contactUsMapper.toDto(contactUsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public ContactUsDto findById(Long id) {
        ContactUs contactUs = contactUsRepository.findById(id).orElseGet(ContactUs::new);
        ValidationUtil.isNull(contactUs.getId(), "ContactUs", "id", id);
        return contactUsMapper.toDto(contactUs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContactUsDto create(ContactUs resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return contactUsMapper.toDto(contactUsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ContactUs resources) {
        ContactUs contactUs = contactUsRepository.findById(resources.getId()).orElseGet(ContactUs::new);
        ValidationUtil.isNull(contactUs.getId(), "ContactUs", "id", resources.getId());
        contactUs.copy(resources);
        contactUsRepository.save(contactUs);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            contactUsRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ContactUsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ContactUsDto contactUs : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", contactUs.getName());
            map.put("邮箱", contactUs.getEmail());
            map.put("联系方式", contactUs.getPhone());
            map.put("留言", contactUs.getMessage());
            map.put("状态 1,未完成，2,已完成", contactUs.getStatus());
            map.put("创建时间", contactUs.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public ContactUs findByPhone(String phone) {
        return contactUsRepository.findByPhone(phone);
    }

}