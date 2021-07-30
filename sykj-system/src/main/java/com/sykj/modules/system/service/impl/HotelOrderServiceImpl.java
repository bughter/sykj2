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

import com.sykj.modules.system.domain.HotelOrder;
import com.sykj.utils.ValidationUtil;
import com.sykj.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.sykj.modules.system.repository.HotelOrderRepository;
import com.sykj.modules.system.service.HotelOrderService;
import com.sykj.modules.system.service.dto.HotelOrderDto;
import com.sykj.modules.system.service.dto.HotelOrderQueryCriteria;
import com.sykj.modules.system.service.mapstruct.HotelOrderMapper;
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
* @date 2021-07-19
**/
@Service
@RequiredArgsConstructor
public class HotelOrderServiceImpl implements HotelOrderService {

    private final HotelOrderRepository hotelOrderRepository;
    private final HotelOrderMapper hotelOrderMapper;

    @Override
    public Map<String,Object> queryAll(HotelOrderQueryCriteria criteria, Pageable pageable){
        Page<HotelOrder> page = hotelOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(hotelOrderMapper::toDto));
    }

    @Override
    public List<HotelOrderDto> queryAll(HotelOrderQueryCriteria criteria){
        return hotelOrderMapper.toDto(hotelOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public HotelOrderDto findById(String id) {
        HotelOrder hotelOrder = hotelOrderRepository.findById(id).orElseGet(HotelOrder::new);
        ValidationUtil.isNull(hotelOrder.getId(),"HotelOrder","id",id);
        return hotelOrderMapper.toDto(hotelOrder);
    }
    @Override
    public HotelOrder findHotelOrderByOrderNo(String orderNo){
        return hotelOrderRepository.findHotelOrderByOrderNo(orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HotelOrderDto create(HotelOrder resources) {
        resources.setId(IdUtil.simpleUUID()); 
        return hotelOrderMapper.toDto(hotelOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HotelOrder resources) {
        HotelOrder hotelOrder = hotelOrderRepository.findById(resources.getId()).orElseGet(HotelOrder::new);
        ValidationUtil.isNull( hotelOrder.getId(),"HotelOrder","id",resources.getId());
        hotelOrder.copy(resources);
        hotelOrderRepository.save(hotelOrder);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            hotelOrderRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HotelOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HotelOrderDto hotelOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单用户备注", hotelOrder.getRemark());
            map.put("酒店订单号", hotelOrder.getOrderNo());
            map.put("预订房间数量", hotelOrder.getNumberOfRooms());
            map.put("间夜数量", hotelOrder.getNumberOfNights());
            map.put("最晚到底时间", hotelOrder.getArriveTime());
            map.put("入住日期", hotelOrder.getCheckInDate());
            map.put("离店日期", hotelOrder.getCheckOutDate());
            map.put("入住人姓名，多个入住人姓名之间用‘|’分割", hotelOrder.getGuestNames());
            map.put("订单总金额", hotelOrder.getOrderAmount());
            map.put("订单状态（1=待付款，2=待确认，3=已确认，4=确认失败，5=已完成，6=取消中，7=取消失败，8=已取消）", hotelOrder.getOrderStatus());
            map.put("支付状态（0=待支付，1=已支付）", hotelOrder.getPayStatus());
            map.put("支付时间(支付成功才有值，格式：yyyy-MM-dd HH:mm:ss)", hotelOrder.getPayTime());
            map.put("支付方式(0未支付 1虚拟钱包支付 2支付宝 3微信 暂时只支持虚拟钱包支付)", hotelOrder.getPaymentType());
            map.put("交易单号", hotelOrder.getTransactionNo());
            map.put("订房失败或取消成功，退款金额", hotelOrder.getRefundAmount());
            map.put("订房失败或取消成功需退款，退款成功时间", hotelOrder.getRefundSuccessTime());
            map.put("退款交易单号", hotelOrder.getRefundTransactionNo());
            map.put("退款交易方式 (对应枚举TransactionMethods值:0未支付 1钱包支付 2支付宝 3微信)", hotelOrder.getRefundTransactionMethods());
            map.put("酒店ID", hotelOrder.getHotelId());
            map.put("酒店名称", hotelOrder.getHotelName());
            map.put("酒店地址", hotelOrder.getHotelAddress());
            map.put("房型名称", hotelOrder.getRoomTypeName());
            map.put("床型", hotelOrder.getBedTypeName());
            map.put("产品(价格计划)ID", hotelOrder.getRatePlanId());
            map.put("每日价格，以“|”分隔，数量需与入住/离店时间对应，如2019-05-20入住，2019-05-22离店，300|200指第1天300元，第2天200元", hotelOrder.getPricePerDay());
            map.put("早餐规则描述", hotelOrder.getBreakfastDesc());
            map.put("取消政策类型(1不可取销,2限时取消,3收费取消)", hotelOrder.getCancelPolicyType());
            map.put("取消政策描述", hotelOrder.getCancelDesc());
            map.put("酒店确认号(可能为空，因为有的酒店不需要确认号)", hotelOrder.getConfirmationNumber());
            map.put("联系人姓名", hotelOrder.getContactName());
            map.put("联系人手机号", hotelOrder.getContactMobile());
            map.put("联系人邮箱", hotelOrder.getContactEmail());
            map.put("支付时限参考时间", hotelOrder.getPayLimitTime());
            map.put("下单时间", hotelOrder.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}