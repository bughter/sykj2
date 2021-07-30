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
import com.sykj.modules.system.domain.User;
import com.sykj.modules.system.domain.Wallet;
import com.sykj.modules.system.domain.WalletDetail;
import com.sykj.modules.system.repository.CompanyRepository;
import com.sykj.modules.system.repository.UserRepository;
import com.sykj.modules.system.repository.WalletRepository;
import com.sykj.utils.*;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.WalletDetailRepository;
import com.sykj.modules.system.service.WalletDetailService;
import com.sykj.modules.system.service.dto.WalletDetailDto;
import com.sykj.modules.system.service.dto.WalletDetailQueryCriteria;
import com.sykj.modules.system.service.mapstruct.WalletDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
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
* @date 2021-06-22
**/
@Service
@RequiredArgsConstructor
public class WalletDetailServiceImpl implements WalletDetailService {

    private final WalletDetailRepository walletDetailRepository;
    private final WalletDetailMapper walletDetailMapper;
    private final WalletRepository walletRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public Map<String,Object> queryAll(WalletDetailQueryCriteria criteria, Pageable pageable){
        Page<WalletDetail> page = walletDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(walletDetailMapper::toDto));
    }

    @Override
    public Map<String,Object> queryAll2(WalletDetailQueryCriteria criteria, Pageable pageable){
        User user= userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        if(null!=user.getCompany()){
            criteria.setCompany(user.getCompany().getId());
        }
        Page<WalletDetail> page = walletDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(walletDetailMapper::toDto));
    }

    @Override
    public List<WalletDetailDto> queryAll(WalletDetailQueryCriteria criteria){
        return walletDetailMapper.toDto(walletDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WalletDetailDto findById(Integer wdId) {
        WalletDetail walletDetail = walletDetailRepository.findById(wdId).orElseGet(WalletDetail::new);
        ValidationUtil.isNull(walletDetail.getWdId(),"WalletDetail","wdId",wdId);
        return walletDetailMapper.toDto(walletDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletDetailDto create(WalletDetail resources) {
        String companyId=resources.getCompany().getId();
        Company company=companyRepository.findById(companyId).orElseGet(Company::new);
        Wallet wallet=company.getWallet();
        BigDecimal amount=resources.getAmount();
        BigDecimal balance=new BigDecimal(0.00);
        if(null != wallet.getBalance()){
            balance=wallet.getBalance();
        }
        BigDecimal sum=amount.add(balance);
        wallet.setBalance(sum);
        walletRepository.save(wallet);
        resources.setBalance(sum);
        return walletDetailMapper.toDto(walletDetailRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WalletDetail resources) {
        WalletDetail walletDetail = walletDetailRepository.findById(resources.getWdId()).orElseGet(WalletDetail::new);
        ValidationUtil.isNull( walletDetail.getWdId(),"WalletDetail","id",resources.getWdId());
        walletDetail.copy(resources);
        walletDetailRepository.save(walletDetail);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer wdId : ids) {
            walletDetailRepository.deleteById(wdId);
        }
    }

    @Override
    public void download(List<WalletDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WalletDetailDto walletDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" company",  walletDetail.getCompany());
            map.put(" createTime",  walletDetail.getCreateTime());
            map.put(" updateTime",  walletDetail.getUpdateTime());
            map.put(" createBy",  walletDetail.getCreateBy());
            map.put(" updateBy",  walletDetail.getUpdateBy());
            map.put("转账金额", walletDetail.getAmount());
            map.put("余额", walletDetail.getBalance());
            map.put(" type",  walletDetail.getType());
            map.put("凭证", walletDetail.getReceipt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}