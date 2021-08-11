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

import com.sykj.modules.system.domain.Department;
import com.sykj.modules.system.domain.User;
import com.sykj.modules.system.repository.UserRepository;
import com.sykj.utils.*;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.DepartmentRepository;
import com.sykj.modules.system.service.DepartmentService;
import com.sykj.modules.system.service.dto.DepartmentDto;
import com.sykj.modules.system.service.dto.DepartmentQueryCriteria;
import com.sykj.modules.system.service.mapstruct.DepartmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
* @date 2021-08-03
**/
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;

    @Override
    public Map<String,Object> queryAll(DepartmentQueryCriteria criteria, Pageable pageable){
        Page<Department> page = departmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(departmentMapper::toDto));
    }

    @Override
    public Map<String,Object> getDepts(DepartmentQueryCriteria criteria, Pageable pageable){
        User user= userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if(null!=user.getCompany()){
            criteria.setCompanyId(user.getCompany().getId());
        }
        Page<Department> page = departmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(departmentMapper::toDto));
    }


    @Override
    public List<DepartmentDto> queryAll(DepartmentQueryCriteria criteria){
        return departmentMapper.toDto(departmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id).orElseGet(Department::new);
        ValidationUtil.isNull(department.getId(),"Department","id",id);
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepartmentDto create(Department resources) {
//        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
//        resources.setId(snowflake.nextId());
        User user= userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if(null!=user.getCompany()){
            resources.setCompany(user.getCompany());
        }
        return departmentMapper.toDto(departmentRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Department resources) {
        Department department = departmentRepository.findById(resources.getId()).orElseGet(Department::new);
        ValidationUtil.isNull( department.getId(),"Department","id",resources.getId());
        department.copy(resources);
        departmentRepository.save(department);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            departmentRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DepartmentDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DepartmentDto department : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部门名称", department.getName());
            map.put("创建人", department.getCreateBy());
            map.put("最后更新", department.getUpdateBy());
            map.put("创建时间", department.getCreateTime());
            map.put("更新时间", department.getUpdateTime());
            map.put("负责人", department.getManager());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}