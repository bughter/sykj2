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

import com.sykj.modules.system.domain.RuleType;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.RuleTypeRepository;
import com.sykj.modules.system.service.RuleTypeService;
import com.sykj.modules.system.service.dto.RuleTypeDto;
import com.sykj.modules.system.service.dto.RuleTypeQueryCriteria;
import com.sykj.modules.system.service.mapstruct.RuleTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
* @date 2021-04-08
**/
@Service
@RequiredArgsConstructor
public class RuleTypeServiceImpl implements RuleTypeService {

    private final RuleTypeRepository ruleTypeRepository;
    private final RuleTypeMapper ruleTypeMapper;

    @Override
    public Map<String,Object> queryAll(RuleTypeQueryCriteria criteria, Pageable pageable){
        Page<RuleType> page = ruleTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ruleTypeMapper::toDto));
    }

    @Override
    public List<RuleTypeDto> queryAll(RuleTypeQueryCriteria criteria){
        return ruleTypeMapper.toDto(ruleTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public RuleTypeDto findById(Integer id) {
        RuleType ruleType = ruleTypeRepository.findById(id).orElseGet(RuleType::new);
        ValidationUtil.isNull(ruleType.getId(),"RuleType","id",id);
        return ruleTypeMapper.toDto(ruleType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleTypeDto create(RuleType resources) {
        return ruleTypeMapper.toDto(ruleTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RuleType resources) {
        RuleType ruleType = ruleTypeRepository.findById(resources.getId()).orElseGet(RuleType::new);
        ValidationUtil.isNull( ruleType.getId(),"RuleType","id",resources.getId());
        ruleType.copy(resources);
        ruleTypeRepository.save(ruleType);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            ruleTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RuleTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RuleTypeDto ruleType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型名称", ruleType.getRuleTypeName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}