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

import com.sykj.modules.system.domain.Form;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.FormRepository;
import com.sykj.modules.system.service.FormService;
import com.sykj.modules.system.service.dto.FormDto;
import com.sykj.modules.system.service.dto.FormQueryCriteria;
import com.sykj.modules.system.service.mapstruct.FormMapper;
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
* @date 2021-05-11
**/
@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    @Override
    public Map<String,Object> queryAll(FormQueryCriteria criteria, Pageable pageable){
        Page<Form> page = formRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(formMapper::toDto));
    }

    @Override
    public List<FormDto> queryAll(FormQueryCriteria criteria){
        return formMapper.toDto(formRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public FormDto findById(Integer id) {
        Form form = formRepository.findById(id).orElseGet(Form::new);
        ValidationUtil.isNull(form.getId(),"Form","id",id);
        return formMapper.toDto(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormDto create(Form resources) {
        return formMapper.toDto(formRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Form resources) {
        Form form = formRepository.findById(resources.getId()).orElseGet(Form::new);
        ValidationUtil.isNull( form.getId(),"Form","id",resources.getId());
        form.copy(resources);
        formRepository.save(form);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            formRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<FormDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FormDto form : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", form.getName());
            map.put("类型", form.getType());
            map.put("表单标识", form.getFormKey());
            map.put("表单数据", form.getFormJson());
            map.put("创建人", form.getCreateBy());
            map.put("创建时间", form.getCreateTime());
            map.put("更新时间", form.getUpdateTime());
            map.put("删除标识", form.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}