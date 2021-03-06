package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.model.info.InformationSearch;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.service.info.InfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Service
public class InfoServiceImpl implements InfoService {


    private Log logger = LogFactory.getLog(InfoServiceImpl.class);

    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private EntityManager entityManager;


    public long infoListsCount(boolean disable) {
        return infoRepository.countByIsDisable(disable);
    }

    public List<Info> findListsByWord(String title) {
        return infoRepository.findByTitleLike(title);
    }


    @Override
    public Info findOneByIdAndCustomerId(Long id,Long customerId){
        Info info;
        if (id != null && id != 0){
            info = infoRepository.findOneByIdAndCustomerIdAndIsDisableFalse(id,customerId);
        }else {
            info = new Info();
        }
        return info;
    }

    @Override
    public Info findOneById(Long id) {
        Info info;
        if (id != null && id != 0){
            info = infoRepository.findOneByIdAndIsDisableFalse(id);
        }else {
            info = new Info();
        }
        return info;
    }

    public Info infoSave(Info info) {
        Info newInfo;
        if (info.getId() != null && info.getId() != 0) {
            newInfo = infoRepository.findOne(info.getId());
        } else {
            newInfo = new Info();
            newInfo.setCustomerId(info.getCustomerId());
            newInfo.setCreateTime(LocalDateTime.now());
        }
        newInfo.setTitle(info.getTitle());
        newInfo.setIntroduce(info.getIntroduce());
        newInfo.setContent(info.getContent());
        if(!StringUtils.isEmpty(info.getImageUrl())){
            newInfo.setImageUrl(info.getImageUrl());
        }
        newInfo.setThumbnailImageUrl(info.getThumbnailImageUrl());
        newInfo.setStatus(info.isStatus());
        newInfo.setDisable(info.isDisable());
        newInfo.setExtend(info.isExtend());
        return infoRepository.save(newInfo);

    }

    @Override
    public boolean deleteInfo(Long customerId, Long id) {
        Info info = infoRepository.findOneByIdAndCustomerIdAndIsDisableFalse(id,customerId);
        if(info != null){
            info.setDisable(true);
            infoRepository.save(info);
            return true;
        }
        return false;
    }

    public Page<Info> infoList(InformationSearch informationSearch) {
        Pageable pageable = new PageRequest(informationSearch.getPageNo()-1, informationSearch.getPageSize(),new Sort(
            new Sort.Order(Sort.Direction.DESC,"createTime")
        ));

        Page<Info> infoPage = infoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(informationSearch.getSearchCondition())){
                list.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + informationSearch.getSearchCondition() + "%"));
            }
            if((!StringUtils.isEmpty(informationSearch.getStartDate()))&&(!StringUtils.isEmpty(informationSearch.getEndDate()))){
                list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),informationSearch.getStartDate()));
                list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(LocalDateTime.class),informationSearch.getEndDate()));
            }
            list.add(criteriaBuilder.equal(root.get("customerId").as(Long.class), informationSearch.getCustomerId()));
            list.add(criteriaBuilder.equal(root.get("isDisable").as(boolean.class), informationSearch.getDisable()));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        infoPage.getContent().stream().forEach(s->{
            s.setInfoBrowseNum(infoBrowseRepository.countByInfoId(s.getId()));
        });
        return infoPage;
    }

    @Override
    public List<Object[]> queryInfoWithBrowse(InformationSearch informationSearch) {
        StringBuilder sql = new StringBuilder("SELECT i.title, i.introduce, i.createTime, i.isStatus, i.isExtend, count(b) AS infoBrowseNum ");
        sql.append("FROM Info i LEFT JOIN InfoBrowse b ON i.id = b.infoId ");
        sql.append("WHERE i.customerId = :customerId ");
        if (!StringUtils.isEmpty(informationSearch.getSearchCondition()))
            sql.append("and i.title like CONCAT('%',:title,'%') ");
        if((!StringUtils.isEmpty(informationSearch.getStartDate()))&&(!StringUtils.isEmpty(informationSearch.getEndDate()))){
            sql.append(" and i.createTime >= :startDate AND i.createTime <= :endDate ");
        }
        sql.append("GROUP BY  i.title, i.introduce, i.createTime, i.isStatus, i.isExtend ");
        sql.append("ORDER BY i.createTime DESC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("customerId", informationSearch.getCustomerId());
        if (!StringUtils.isEmpty(informationSearch.getSearchCondition()))
            query.setParameter("title", informationSearch.getSearchCondition());
        if((!StringUtils.isEmpty(informationSearch.getStartDate()))&&(!StringUtils.isEmpty(informationSearch.getEndDate()))) {
            query.setParameter("startDate", informationSearch.getStartDate());
            query.setParameter("endDate", informationSearch.getEndDate());
        }
        return query.getResultList();
    }

}
