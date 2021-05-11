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

import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.RuleRepository;
import com.sykj.modules.system.service.RuleService;
import com.sykj.modules.system.service.dto.RuleDto;
import com.sykj.modules.system.service.dto.RuleQueryCriteria;
import com.sykj.modules.system.service.mapstruct.RuleMapper;
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
import com.sykj.modules.system.domain.Rule;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author czy
* @date 2021-04-09
**/
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final RuleMapper ruleMapper;

    @Override
    public Map<String,Object> queryAll(RuleQueryCriteria criteria, Pageable pageable){
        Page<Rule> page = ruleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ruleMapper::toDto));
    }

    @Override
    public List<RuleDto> queryAll(RuleQueryCriteria criteria){
        return ruleMapper.toDto(ruleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public RuleDto findById(Integer rId) {
        Rule rule = ruleRepository.findById(rId).orElseGet(Rule::new);
        ValidationUtil.isNull(rule.getRId(),"Rule","rId",rId);
        return ruleMapper.toDto(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleDto create(Rule resources) {
        return ruleMapper.toDto(ruleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Rule resources) {
        Rule rule = ruleRepository.findById(resources.getRId()).orElseGet(Rule::new);
        ValidationUtil.isNull( rule.getRId(),"Rule","id",resources.getRId());
        rule.copy(resources);
        ruleRepository.save(rule);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer rId : ids) {
            ruleRepository.deleteById(rId);
        }
    }

    @Override
    public void download(List<RuleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RuleDto rule : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("规则名称", rule.getRName());
            map.put("规则类型", rule.getRuleType());
            map.put("城市级别", rule.getCityRank());
            map.put("创建时间", rule.getCreateTime());
            map.put("更新时间", rule.getUpdateTime());
            map.put("创建者", rule.getCreateBy());
            map.put("最后更新", rule.getUpdateBy());
            map.put("所属公司", rule.getCompany());
            map.put("价格上限", rule.getCompany());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}