package com.rtucloud.cs.proxy.dao.repository;

import com.rtucloud.cs.proxy.dao.entity.FrontendPortInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrontendPortRepository extends JpaRepository<FrontendPortInfo, Integer> {
}
