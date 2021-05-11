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

import com.sykj.modules.system.domain.CompanyType;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.CompanyTypeRepository;
import com.sykj.modules.system.service.CompanyTypeService;
import com.sykj.modules.system.service.dto.CompanyTypeDto;
import com.sykj.modules.system.service.dto.CompanyTypeQueryCriteria;
import com.sykj.modules.system.service.mapstruct.CompanyTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
* @website https://el-admin.vip
* @description 服务实现
* @author czy
* @date 2021-04-28
**/
@Service
@RequiredArgsConstructor
public class CompanyTypeServiceImpl implements CompanyTypeService {

    private final CompanyTypeRepository companyTypeRepository;
    private final CompanyTypeMapper companyTypeMapper;

    @Override
    public Map<String,Object> queryAll(CompanyTypeQueryCriteria criteria, Pageable pageable){
        Page<CompanyType> page = companyTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyTypeMapper::toDto));
    }

    @Override
    public List<CompanyTypeDto> queryAll(CompanyTypeQueryCriteria criteria){
        return companyTypeMapper.toDto(companyTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyTypeDto findById(String companyTypeId) {
        CompanyType companyType = companyTypeRepository.findById(companyTypeId).orElseGet(CompanyType::new);
        ValidationUtil.isNull(companyType.getCompanyTypeId(),"CompanyType","companyTypeId",companyTypeId);
        return companyTypeMapper.toDto(companyType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyTypeDto create(CompanyType resources) {
        resources.setCompanyTypeId(IdUtil.simpleUUID()); 
        return companyTypeMapper.toDto(companyTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyType resources) {
        CompanyType companyType = companyTypeRepository.findById(resources.getCompanyTypeId()).orElseGet(CompanyType::new);
        ValidationUtil.isNull( companyType.getCompanyTypeId(),"CompanyType","id",resources.getCompanyTypeId());
        companyType.copy(resources);
        companyTypeRepository.save(companyType);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String companyTypeId : ids) {
            companyTypeRepository.deleteById(companyTypeId);
        }
    }

    @Override
    public void download(List<CompanyTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyTypeDto companyType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", companyType.getName());
            map.put("累计", companyType.getCount());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}