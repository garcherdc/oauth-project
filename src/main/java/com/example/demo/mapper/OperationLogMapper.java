package com.example.demo.mapper;



import com.example.demo.entity.OperationLogDO;
import com.example.demo.req.OperationLogQueryReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OperationLogDO record);

    OperationLogDO selectByPrimaryKey(Long id);
    List<OperationLogDO> queryByCondition(OperationLogQueryReq operationLogQueryReq);
    int updateByPrimaryKeySelective(OperationLogDO record);

}