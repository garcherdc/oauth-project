package com.example.demo.mapping;



import com.example.demo.entity.OperationLogDO;
import com.example.demo.entity.OperationLogDTO;
import com.example.demo.entity.OperationLogExcelVO;
import com.example.demo.entity.OperationLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {TypeConversionWorker.class}, componentModel = "spring")
public interface OperationLogMapping {

    OperationLogDO toDO(OperationLogDTO operationLogDTO);

    List<OperationLogVO> toVOList(List<OperationLogDO> operationLogDOList);
    @Mapping(source = "operation", target = "operation", qualifiedByName = "convertOperation")
    OperationLogVO toVO(OperationLogDO operationLogDO);

    List<OperationLogExcelVO> toExcelVOList(List<OperationLogDO> operationLogDOList);
    @Mapping(source = "operation", target = "operation", qualifiedByName = "convertOperation")
    @Mapping(source = "operationTime", target = "operationTime", qualifiedByName = "convertOperationTime")
    OperationLogExcelVO toExcelVO(OperationLogDO operationLogDO);
}