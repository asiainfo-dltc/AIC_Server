package com.asiainfo.dao.until;

import com.asiainfo.model.DacpMonitorProcessEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DacpMonitorProcessDao {
public List<DacpMonitorProcessEntity> getDacpMonitorProcess(@Param("dacpMonitorProcessEntity")DacpMonitorProcessEntity dacpMonitorProcessEntity);
}
