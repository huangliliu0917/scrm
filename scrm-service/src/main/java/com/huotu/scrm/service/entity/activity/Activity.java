package com.huotu.scrm.service.entity.activity;

import com.huotu.scrm.common.ienum.ActEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动表
 * <p>
 * Created by montage on 2017/7/11.
 */

@Entity
@Getter
@Setter
@Table(name = "SCRM_Act")
public class Activity {

    /**
     * 自增Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long actId;


    /**
     * 获取活动相关的奖品
     */
    @OneToMany(mappedBy = "activity",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval = true)
    @OrderBy("sort asc")
    private List<ActPrize> actPrizes;

    /**
     * 商户Id
     */
    @Column(name = "Customer_Id")
    private Long customerId;

    /**
     * 活动标题
     */
    @Column(name = "Act_Title")
    private String actTitle;

    /**
     * 活动类型
     * 0:大转盘
     */
    @Column(name = "Act_Type")
    private ActEnum.Activity actType;

    /**
     * 开始时间
     */
    @Column(name = "Start_Date", columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @Column(name = "End_Date", columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /**
     * 开启状态
     * 0:禁用, 1:启用
     */
    @Column(name = "Open_Status")
    private boolean openStatus;

    /**
     * 每次游戏消耗积分
     */
    @Column(name = "Game_Costly_Score")
    private int gameCostlyScore;

    /**
     * 规则简介
     */
    @Column(name = "Rule_Desc")
    private String ruleDesc;

    /**
     * 概率简介
     */
    @Column(name = "Rate_Desc")
    private String rateDesc;

    /**
     * 是否删除
     * 0:未删除,1:已删除，默认是0
     */
    @Column(name = "Is_Delete")
    private boolean isDelete;

    @Override
    public String toString() {
        return "Activity{" +
                "actId=" + actId +
                ", customerId=" + customerId +
                ", actTitle='" + actTitle + '\'' +
                ", actType=" + actType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", openStatus=" + openStatus +
                ", gameCostlyScore=" + gameCostlyScore +
                ", ruleDesc='" + ruleDesc + '\'' +
                ", rateDesc='" + rateDesc + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }

    /**
     * 活动还未开始
     * @return true 表示活动开启
     */
    public boolean activeBegin() {
        LocalDateTime now =  LocalDateTime.now();
        return  openStatus && now.isAfter(startDate);
    }

    /**
     * 活动结束了
     * @return true 表示活动开启
     */
    public boolean activeEnd() {
        LocalDateTime now =  LocalDateTime.now();
        return now.isBefore(endDate);
    }


}
