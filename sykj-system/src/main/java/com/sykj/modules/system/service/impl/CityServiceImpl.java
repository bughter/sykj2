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

import com.sykj.modules.system.domain.City;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.CityRepository;
import com.sykj.modules.system.service.CityService;
import com.sykj.modules.system.service.dto.CityDto;
import com.sykj.modules.system.service.dto.CityQueryCriteria;
import com.sykj.modules.system.service.mapstruct.CityMapper;
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
* @date 2021-04-07
**/
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public Map<String,Object> queryAll(CityQueryCriteria criteria, Pageable pageable){
        Page<City> page = cityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(cityMapper::toDto));
    }

    @Override
    public List<CityDto> queryAll(CityQueryCriteria criteria){
        return cityMapper.toDto(cityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CityDto findById(Integer cid) {
        City city = cityRepository.findById(cid).orElseGet(City::new);
        ValidationUtil.isNull(city.getCid(),"City","cid",cid);
        return cityMapper.toDto(city);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CityDto create(City resources) {
        return cityMapper.toDto(cityRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(City resources) {
        City city = cityRepository.findById(resources.getCid()).orElseGet(City::new);
        ValidationUtil.isNull( city.getCid(),"City","id",resources.getCid());
        city.copy(resources);
        cityRepository.save(city);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer cid : ids) {
            cityRepository.deleteById(cid);
        }
    }

    @Override
    public void download(List<CityDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CityDto city : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("城市名称", city.getCity());
            map.put(" pid",  city.getPid());
            map.put("城市等级", city.getRank());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}