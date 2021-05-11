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

import com.sykj.modules.system.domain.Company;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.CompanyRepository;
import com.sykj.modules.system.service.CompanyService;
import com.sykj.modules.system.service.dto.CompanyDto;
import com.sykj.modules.system.service.dto.CompanyQueryCriteria;
import com.sykj.modules.system.service.mapstruct.CompanyMapper;
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
* @date 2021-04-23
**/
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public Map<String,Object> queryAll(CompanyQueryCriteria criteria, Pageable pageable){
        Page<Company> page = companyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyMapper::toDto));
    }

    @Override
    public List<CompanyDto> queryAll(CompanyQueryCriteria criteria){
        return companyMapper.toDto(companyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyDto findById(String id) {
        Company company = companyRepository.findById(id).orElseGet(Company::new);
        ValidationUtil.isNull(company.getId(),"Company","id",id);
        return companyMapper.toDto(company);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyDto create(Company resources) {
        resources.setId(IdUtil.simpleUUID()); 
        return companyMapper.toDto(companyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Company resources) {
        Company company = companyRepository.findById(resources.getId()).orElseGet(Company::new);
        ValidationUtil.isNull( company.getId(),"Company","id",resources.getId());
        company.copy(resources);
        companyRepository.save(company);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            companyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyDto company : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("公司名称", company.getCompanyName());
            map.put("营业执照", company.getCompanyLicense());
            map.put("法人身份证", company.getCompanyIdcard());
            map.put("创建时间", company.getCreateTime());
            map.put("更新时间", company.getUpdateTime());
            map.put("创建者", company.getCraeteBy());
            map.put("最后更新", company.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}