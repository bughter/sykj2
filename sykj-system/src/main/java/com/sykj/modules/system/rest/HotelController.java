package com.sykj.modules.system.rest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sykj.annotation.AnonymousAccess;
import com.sykj.annotation.Log;
import com.sykj.modules.system.domain.HotelOrder;
import com.sykj.modules.system.service.HotelOrderService;
import com.sykj.modules.system.service.dto.*;
import com.sykj.modules.system.service.mapstruct.HotelOrderMapper;
import com.sykj.utils.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

@RestController
@RequiredArgsConstructor
@PropertySource({"classpath:generator.properties"})
@Api(tags = "api/酒店")
@RequestMapping("/api/hotel")
public class HotelController {
    @Value("${wx.panhe.appKey}")
    private String appKey;
    @Value("${wx.panhe.secretKey}")
    private String secretKey;
    @Value("${wx.panhe.backUrl}")
    private String backUrl;
    @Value("${localhost.realm}")
    private String realm;

    private final HotelOrderService hotelOrderService;
    private final HotelOrderMapper hotelOrderMapper;

    @GetMapping
    @Log("查询酒店")
    @ApiOperation("查询酒店")
    @AnonymousAccess
    public String getHotel(String cityName, String checkInDate, String checkOutDate,
                           @RequestParam(defaultValue = "1") String pageIndex,
                           @RequestParam(defaultValue = "10") String pageSize, String sort, String star,
                           String startPrice, String endPrice, String queryText,
                           String longitude, String latitude) {
        // 请求参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appKey", appKey);
        map.put("cityName", cityName.replaceAll("市", ""));
        map.put("checkInDate", checkInDate);
        map.put("checkOutDate", checkOutDate);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("sort", sort);
        map.put("star", star.toString());
        map.put("minPrice", startPrice);
        map.put("maxPrice", endPrice);
        map.put("queryText", queryText);
        if (!longitude.isEmpty() && !latitude.isEmpty()) {
            map.put("longitude", longitude);
            map.put("latitude", latitude);
        }

        //请求路径
        String url = "http://api.panhe.net/hotel/searchHotelList";
        //发送请求
        String result = HttpUtils.doGet(url, map);
        HotelDto dto = JSONObject.parseObject(result, HotelDto.class);

        Map<String, Object> resultMap = new LinkedHashMap<>(4);
        if (null != dto) {
            HotelChildDto hotel = dto.getData();
            resultMap.put("success", dto.getSuccess());
            resultMap.put("msg", dto.getMsg());
            if (hotel != null) {
                resultMap.put("content", hotel.getHotelList());
                resultMap.put("totalElements", hotel.getCount());
            } else {
                resultMap.put("content", null);
                resultMap.put("totalElements", 0);
            }
        }
        return JSONObject.toJSONString(resultMap);
    }

    @GetMapping(value = "getRoom")
    @Log("查询房间")
    @ApiOperation("查询房间")
    @AnonymousAccess
    public String getRoom(String hotelID, String checkInDate, String checkOutDate) {// 创建Httpclient对象

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appKey", appKey);
        map.put("hotelID", hotelID);
        map.put("checkInDate", checkInDate);
        map.put("checkOutDate", checkOutDate);
        //请求路径
        String url = "http://api.panhe.net/hotel/queryRoomAndRatePlan";
        //发送请求
        String result = HttpUtils.doGet(url, map);
        RoomDto dto = JSONObject.parseObject(result, RoomDto.class);

        Map<String, Object> resultMap = new LinkedHashMap<>(4);
        if (null != dto) {
            RoomChildDto room = dto.getData();
            resultMap.put("success", dto.getSuccess());

            resultMap.put("msg", dto.getMsg());
            resultMap.put("content", room.getRooms());
        }
        return JSONObject.toJSONString(resultMap);
    }

    @GetMapping(value = "getHotelDetail")
    @Log("酒店详情")
    @ApiOperation("酒店详情")
    @AnonymousAccess
    public String getHotelDetail(String hotelID) {// 创建Httpclient对象

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appKey", appKey);
        map.put("hotelID", hotelID);
        //请求路径
        String url = "http://api.panhe.net/hotel/getHotelDetail";
        //发送请求
        String result = HttpUtils.doGet(url, map);
        HotelDetailDto dto = JSONObject.parseObject(result, HotelDetailDto.class);

        Map<String, Object> resultMap = new LinkedHashMap<>(4);
        if (null != dto) {
            resultMap.put("success", dto.getSuccess());
            resultMap.put("msg", dto.getMsg());
            resultMap.put("content", dto.getData());
        }
        return JSONObject.toJSONString(resultMap);
    }

    @GetMapping(value = "/createHotelOrder")
    @Log("创建酒店订单")
    @ApiOperation("创建酒店订单")
    @AnonymousAccess
    public String createHotelOrder(String hotelID, String ratePlanID, String roomNum,
                                   String checkInDate, String checkOutDate, String[] guestNames, String arriveTime,
                                   String orderAmount, String contactName, String contactMobile, String orderRemark,
                                   String realAmount,String flag) throws Exception {
        if(flag.equals("false")){
            return JSONObject.parseObject(StringEscapeUtils.unescapeJava("{\"success\":false,\"msg\":\"钱包余额不足！\"}")).toJSONString();
        }
        //新建本地订单
        HotelOrder hotelOrder = new HotelOrder();
        hotelOrder.setHotelId(new Integer(hotelID));
        hotelOrder.setRatePlanId(ratePlanID);
        hotelOrder.setNumberOfRooms(new Integer(roomNum));
        hotelOrder.setCheckInDate(checkInDate);
        hotelOrder.setCheckOutDate(checkOutDate);
        hotelOrder.setGuestNames(Arrays.toString(guestNames));
        hotelOrder.setArriveTime(arriveTime);
        hotelOrder.setOrderAmount(new BigDecimal(orderAmount));
        hotelOrder.setContactName(contactName);
        hotelOrder.setContactMobile(contactMobile);
        hotelOrder.setRemark(orderRemark);
        hotelOrder.setRealAmount(new BigDecimal(realAmount));
        HotelOrderDto hotelOrderDto = hotelOrderService.create(hotelOrder);
        String customerOrderNo = hotelOrderDto.getId();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appKey", appKey);
        map.put("customerOrderNo", customerOrderNo);
        map.put("hotelID", hotelID);
        map.put("ratePlanID", ratePlanID);
        map.put("roomNum", roomNum);
        map.put("checkInDate", checkInDate);
        map.put("checkOutDate", checkOutDate);
        map.put("guestNames", guestNames);
        map.put("arriveTime", arriveTime);
        map.put("orderAmount", orderAmount);
        map.put("contactName", contactName);
        map.put("contactMobile", contactMobile);
        map.put("orderRemark", orderRemark);
        map.put("callBackUrl", backUrl);
        //请求路径
//        String url = "http://api.panhe.net/hotel/createOrder";
        //发送请求
//        String result = HttpUtils.doPost(url, map);
        URL url = new URL("http://api.panhe.net/hotel/createOrder");
        String result = HttpUtils.connectPanHeHotel(map, url, "POST");
        Boolean success = JSONObject.parseObject(result).getBoolean("success");
        if (success == true) {
            JSONObject jsondata = JSONObject.parseObject(result).getJSONObject("data");
            String orderNo = jsondata.getString("orderNo");
            HotelOrder hotelOrderData = hotelOrderMapper.toEntity(hotelOrderService.findById(customerOrderNo));
            hotelOrderData.setOrderNo(orderNo);
            hotelOrderService.update(hotelOrderData);
//            this.pay(orderNo, orderAmount);
        }

        return JSONObject.parseObject(result).toJSONString();
    }

    @GetMapping(value = "/cancelHotelOrder")
    @Log("取消酒店订单")
    @ApiOperation("取消酒店订单")
    @AnonymousAccess
    public void cancelHotelOrder(String orderNo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appKey", appKey);
        map.put("orderNo", orderNo);
        String url = "http://api.panhe.net/hotel/cancelOrder";
        String result = HttpUtils.doPost(url, map);
        Boolean success = JSONObject.parseObject(result).getBoolean("success");
        if (success == true) {
            this.queryHotelOrder(orderNo);
        }
    }

    @GetMapping(value = "/queryHotelOrder")
    @Log("查询酒店订单")
    @ApiOperation("查询酒店订单")
    @AnonymousAccess
    public void queryHotelOrder(String orderNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appKey", appKey);
        map.put("orderNo", orderNo);
        String url = "http://api.panhe.net/hotel/getOrderDetail";
        //发送请求
        String result = HttpUtils.doGet(url, map);
        Boolean success = JSONObject.parseObject(result).getBoolean("success");
        if (success == true) {
            JSONObject jsondata = JSONObject.parseObject(result).getJSONObject("data");
            HotelOrder hotelOrder = hotelOrderService.findHotelOrderByOrderNo(orderNo);
            String id = hotelOrder.getId();
            hotelOrder = JSON.parseObject(jsondata.toString(), HotelOrder.class);
            hotelOrder.setId(id);
            hotelOrderService.update(hotelOrder);
        }

    }

    @GetMapping(value = "/payHotelOrder")
    @Log("支付代扣")
    @ApiOperation("支付代扣")
    @AnonymousAccess
    public void pay(String orderNo, String payAmount) throws Exception {
        String sign = secretKey + orderNo + payAmount + secretKey;
        String signMd5 = getMd5(sign);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appKey", appKey);
        map.put("orderNo", orderNo);
        map.put("payAmount", payAmount);
        map.put("sign", signMd5);
        String url = "http://api.panhe.net/hotel/autoPay";
        //发送请求
        String result = HttpUtils.doPost(url, map);
        Boolean success = JSONObject.parseObject(result).getBoolean("success");
        if (success == true) {
            this.queryHotelOrder(orderNo);
        }

    }

    @PostMapping(value = "/callBack")
    @Log("酒店回调")
    @ApiOperation("酒店回调")
    @AnonymousAccess
    public void callBack(String orderNo, Integer status, String failMsg) throws Exception {
        if (null != orderNo) {
            this.queryHotelOrder(orderNo);
        }
    }

    @GetMapping(value = "/weixin")
    @Log("weixin")
    @ApiOperation("weixin")
    @AnonymousAccess
    public String weixin(String code) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", "wx02924ef58fb26ca2");
        map.put("secret", "c93ac1485635d6d3b91bcc75f0d9ffa3");
        map.put("grant_type", "authorization_code");
        map.put("js_code", code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        //发送请求
        String result = HttpUtils.doGet(url, map);
        return result;
    }

    @GetMapping(value = "/queryOrder")
    @Log("queryOrder")
    @ApiOperation("queryOrder")
    @AnonymousAccess
    public Map<String,Object> queryOrder(HotelOrderQueryCriteria criteria, Pageable pageable){
        return hotelOrderService.queryAll(criteria,pageable);
    }

    public String getMd5(String str) throws Exception {
        String result = "";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update((str).getBytes("UTF-8"));
        byte b[] = md5.digest();

        int i;
        StringBuffer buf = new StringBuffer("");

        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }

        result = buf.toString();
        return result;
    }


}
