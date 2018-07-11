package com.rtucloud.cs.proxy.dao.repository;

import com.rtucloud.cs.proxy.dao.entity.FrontendPortInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FrontendPortRepository extends JpaRepository<FrontendPortInfo, Integer> {


    @Query(nativeQuery = true,
            value = "SELECT * FROM frontend_port_info LIMIT 0,1")
    FrontendPortInfo findOne();
}
