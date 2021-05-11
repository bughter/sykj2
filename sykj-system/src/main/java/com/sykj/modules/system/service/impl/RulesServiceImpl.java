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

import com.sykj.modules.system.domain.Rules;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.RulesRepository;
import com.sykj.modules.system.service.RulesService;
import com.sykj.modules.system.service.dto.RulesDto;
import com.sykj.modules.system.service.dto.RulesQueryCriteria;
import com.sykj.modules.system.service.mapstruct.RulesMapper;
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
* @date 2021-04-20
**/
@Service
@RequiredArgsConstructor
public class RulesServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;
    private final RulesMapper rulesMapper;

    @Override
    public Map<String,Object> queryAll(RulesQueryCriteria criteria, Pageable pageable){
        Page<Rules> page = rulesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
//        System.out.println(page.getContent().get(0).getRuleSet().size());
        return PageUtil.toPage(page.map(rulesMapper::toDto));
    }

    @Override
    public List<RulesDto> queryAll(RulesQueryCriteria criteria){
        return rulesMapper.toDto(rulesRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public RulesDto findById(Integer id) {
        Rules rules = rulesRepository.findById(id).orElseGet(Rules::new);
        ValidationUtil.isNull(rules.getId(),"Rules","id",id);
        return rulesMapper.toDto(rules);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RulesDto create(Rules resources) {
        return rulesMapper.toDto(rulesRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Rules resources) {
        Rules rules = rulesRepository.findById(resources.getId()).orElseGet(Rules::new);
        ValidationUtil.isNull( rules.getId(),"Rules","id",resources.getId());
        rules.copy(resources);
        rulesRepository.save(rules);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            rulesRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RulesDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RulesDto rules : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("规则集名称", rules.getRulesName());
            map.put("所属公司", rules.getCompany());
            map.put("创建时间", rules.getCreateTime());
            map.put("更新时间", rules.getUpdateTime());
            map.put("创建者", rules.getCreateBy());
            map.put("最后更新", rules.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}