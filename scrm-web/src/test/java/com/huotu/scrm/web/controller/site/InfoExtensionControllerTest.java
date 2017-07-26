package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.model.DayFollowNumInfo;
import com.huotu.scrm.service.model.DayScoreInfo;
import com.huotu.scrm.service.model.DayScoreRankingInfo;
import com.huotu.scrm.service.model.DayVisitorNumInfo;
import com.huotu.scrm.service.model.InfoModel;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by hxh on 2017-07-21.
 */
public class InfoExtensionControllerTest extends CommonTestBase {
    private String baseUrl = "/site/extension";

    /**
     * 测试进入资讯状态
     *
     * @throws Exception
     */
    @Test
    public void getInfoExtension() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/getInfoExtension"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("infoModes"))
                .andReturn();
        System.out.println("................:" + mvcResult.getModelAndView().getViewName());
        List<InfoModel> list = (List<InfoModel>) mvcResult.getModelAndView().getModel().get("infoModes");
        list.forEach(infoModel -> {
            System.out.println("..............:" + infoModel.toString());
        });
    }

    /**
     * 测试今日排名信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreRanking() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/getScoreRanking"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreRankingInfo"))
                .andReturn();
        System.out.println("................:" + mvcResult.getModelAndView().getViewName());
        DayScoreRankingInfo dayScoreRankingInfo = (DayScoreRankingInfo) mvcResult.getModelAndView().getModel().get("dayScoreRankingInfo");
        System.out.println("................:" + dayScoreRankingInfo.toString());
    }

    /**
     * 测试今日积分信息
     *
     * @throws Exception
     */
    @Test
    public void getScoreInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/getScoreInfo"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayScoreInfo"))
                .andReturn();
        System.out.println("................:" + mvcResult.getModelAndView().getViewName());
        DayScoreInfo dayScoreInfo = (DayScoreInfo) mvcResult.getModelAndView().getModel().get("dayScoreInfo");
        System.out.println("................:" + dayScoreInfo.toString());
    }

    /**
     * 测试今日关注信息
     *
     * @throws Exception
     */
    @Test
    public void getFollowInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/getFollowInfo"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayFollowNumInfo"))
                .andReturn();
        System.out.println("................:" + mvcResult.getModelAndView().getViewName());
        DayFollowNumInfo dayFollowNumInfo = (DayFollowNumInfo) mvcResult.getModelAndView().getModel().get("dayFollowNumInfo");
        System.out.println("................:" + dayFollowNumInfo.toString());
    }

    /**
     * 测试今日访问量信息
     *
     * @throws Exception
     */
    @Test
    public void getVisitorInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/getVisitorInfo"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dayVisitorNumInfo"))
                .andReturn();
        System.out.println("................:" + mvcResult.getModelAndView().getViewName());
        DayVisitorNumInfo dayVisitorNumInfo = (DayVisitorNumInfo) mvcResult.getModelAndView().getModel().get("dayVisitorNumInfo");
        System.out.println("................:" + dayVisitorNumInfo.toString());
    }
}