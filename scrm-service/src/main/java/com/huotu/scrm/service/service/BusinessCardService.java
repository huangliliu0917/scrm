package com.huotu.scrm.service.service;

import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.model.SalesmanBusinessCard;

/**
 * Created by Administrator on 2017/7/11.
 */
public interface BusinessCardService {

    /***
     * 获得销售员名片信息
     * @param salesmanId
     * @param customerId
     * @return
     */
    BusinessCard getBusinessCard(Long salesmanId , Long customerId);

    /***
     * 通过salesmanId和customerId获得名片信息和用户信息
     * @param salesmanId 销售员Id
     * @param customerId 商户Id
     * @param followerId 关注者Id
     * @return
     */
    SalesmanBusinessCard getSalesmanBusinessCard(Long salesmanId , Long customerId , Long followerId);

    /***
     * 按照类型更新名片信息
     * @param customerId
     * @param userId
     * @param type
     * @param text
     * @return
     */
    BusinessCard updateBusinessCard(Long customerId , Long userId , BusinessCardUpdateTypeEnum type , String text);

}