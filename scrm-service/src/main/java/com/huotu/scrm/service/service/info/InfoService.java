package com.huotu.scrm.service.service.info;

import com.huotu.scrm.common.utils.InformationSearch;
import com.huotu.scrm.service.entity.info.Info;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoService {


    /**
     * 通过ID 查找对应的资讯
     * @param id
     * @return
     */
    Info findOneById(Long id);

    /**
     * 根据Disable字段查询行数
     *
     * @return
     */
    long infoListsCount(boolean disable);


    /**
     * 根据某一个模糊条件搜索标题查找相应的资讯列表
     */
    List<Info> findListsByWord(String title);


    /**
     * 根据分页条件查找到某一页的资讯列表
     */
    Page<Info> infoSList(InformationSearch informationSearch);


    /**
     * jpa 自带可以不用自己写  saveAndFlush
     * 创建资讯保存到数据库
     */
    @Transactional
    Info infoSave(Info info);


}