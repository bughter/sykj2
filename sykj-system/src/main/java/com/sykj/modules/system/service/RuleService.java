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
package com.sykj.modules.system.service;

import com.sykj.modules.system.service.dto.RuleDto;
import com.sykj.modules.system.service.dto.RuleQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.sykj.modules.system.domain.Rule;

/**
* @website https://el-admin.vip
* @description 服务接口
* @author czy
* @date 2021-04-09
**/
public interface RuleService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RuleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RuleDto>
    */
    List<RuleDto> queryAll(RuleQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param rId ID
     * @return RuleDto
     */
    RuleDto findById(Integer rId);

    /**
    * 创建
    * @param resources /
    * @return RuleDto
    */
    RuleDto create(Rule resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Rule resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<RuleDto> all, HttpServletResponse response) throws IOException;
}