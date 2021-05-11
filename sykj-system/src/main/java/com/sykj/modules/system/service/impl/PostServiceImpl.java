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

import com.sykj.modules.system.domain.Post;
import com.sykj.modules.system.domain.User;
import com.sykj.modules.system.repository.UserRepository;
import com.sykj.utils.*;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.PostRepository;
import com.sykj.modules.system.service.PostService;
import com.sykj.modules.system.service.dto.PostDto;
import com.sykj.modules.system.service.dto.PostQueryCriteria;
import com.sykj.modules.system.service.mapstruct.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
* @date 2021-04-29
**/
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    public Map<String,Object> queryAll(PostQueryCriteria criteria, Pageable pageable){
        Page<Post> page = postRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(postMapper::toDto));
    }

    @Override
    public Map<String,Object> queryAll2(PostQueryCriteria criteria, Pageable pageable){
        User user= userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if(null!=user.getCompany()){
            criteria.setCompanyId(user.getCompany().getId());
        }
        Page<Post> page = postRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(postMapper::toDto));
    }

    @Override
    public List<PostDto> queryAll(PostQueryCriteria criteria){
        return postMapper.toDto(postRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PostDto findById(String postId) {
        Post post = postRepository.findById(postId).orElseGet(Post::new);
        ValidationUtil.isNull(post.getPostId(),"Post","postId",postId);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDto create(Post resources) {
        resources.setPostId(IdUtil.simpleUUID());
        User user= userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if(null!=user.getCompany()){
            resources.setCompany(user.getCompany());
        }
        return postMapper.toDto(postRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Post resources) {
        Post post = postRepository.findById(resources.getPostId()).orElseGet(Post::new);
        ValidationUtil.isNull( post.getPostId(),"Post","id",resources.getPostId());
        post.copy(resources);
        post.setPPost(resources.getPPost());
        postRepository.save(post);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String postId : ids) {
            postRepository.deleteById(postId);
        }
    }

    @Override
    public void download(List<PostDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PostDto post : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("职位名称", post.getPostName());
            map.put("上级职位", post.getPPost());
            map.put("所属公司", post.getCompany());
            map.put("创建时间", post.getCreateTime());
            map.put("更新时间", post.getUpdateTime());
            map.put("创建者", post.getCreateBy());
            map.put("最后更新", post.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}