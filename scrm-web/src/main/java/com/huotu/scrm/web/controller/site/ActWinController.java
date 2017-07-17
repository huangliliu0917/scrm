/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.common.utils.DateUtil;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.service.ActPrizeService;
import com.huotu.scrm.service.service.ActWinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by montage on 2017/7/17.
 */

@Controller
@RequestMapping("/act/prize")
public class ActWinController extends SiteBaseController {

    @Autowired
    private ActPrizeService actPrizeService;

    @Autowired
    private ActWinDetailService actWinDetailService;
    /**
     * 参加抽奖活动
     *
     * @return
     */
    @RequestMapping(value = "/join/act")
    @ResponseBody
    public ApiResult joinAct(
            HttpServletRequest request , String userId, int userScore,
            @RequestParam(required = false,defaultValue = "null" ) String userName,
            @RequestParam(required = false,defaultValue = "null") String userTel)
    {
        //回归算法得到奖品
        int proCount = 100;
        String minRate = "min";
        String maxRate = "max";
        Integer tempInt = 0;
        //待中奖奖品数组
        Map<Long,Map<String,Integer>> prizesMap = new HashMap<>();
        List<ActPrize> actPrizeList = actPrizeService.findAll();
        for (ActPrize actPrize: actPrizeList) {
            Map<String,Integer> oddMap = new HashMap<>();
            oddMap.put(minRate,tempInt);
            tempInt += actPrize.getWinRate();
            oddMap.put(maxRate,tempInt);
            prizesMap.put(actPrize.getPrizeId(),oddMap);
        }
        //得到随机数
        int random = (int) Math.random() * proCount;
        ActPrize prize = null;
        String format = null;
        Set<Long> prizeIds = prizesMap.keySet();
        for (Long prizeId : prizeIds) {
            Map<String, Integer> oddsMap = prizesMap.get(prizeId);
            Integer minNum = oddsMap.get(minRate);
            Integer maxNum = oddsMap.get(maxRate);
            //验证random 在哪件奖品中间
            if (minNum <= random && maxNum > random ) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format = dateFormat.format(now);
                prize = actPrizeService.findByPrizeId(prizeId);
                if (prize.getRemainCount() == 0){
                    prize =actPrizeService.findByPrizeType(false);
                }
                break;
            }
        }
        //TODO 用户积分没有得到 无法计算游戏消耗积分
        //得到用户的Ip地址
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ActWinDetail actWinDetail = new ActWinDetail();
        actWinDetail.setPrize(prize);
        actWinDetail.setIpAddress(ip);
        actWinDetail.setUserId(Long.valueOf(userId));
        actWinDetail.setWin_Time(DateUtil.stringToDate(format));
        actWinDetail.setWinnerName(userName);
        actWinDetail.setWinnerTel(userTel);
        actWinDetail = actWinDetailService.saveActWinDetail(actWinDetail);

        if (actWinDetail != null ){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);
    }

}
