package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse,Long> ,JpaSpecificationExecutor<InfoBrowse> {


    long countByInfoId(Long infoId);


    List<InfoBrowse> findByInfoId(Long infoId);
}