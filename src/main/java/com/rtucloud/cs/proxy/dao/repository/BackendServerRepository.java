package com.rtucloud.cs.proxy.dao.repository;

import com.rtucloud.cs.proxy.dao.entity.BackendServerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackendServerRepository extends JpaRepository<BackendServerInfo, Integer> {


}
