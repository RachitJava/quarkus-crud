package com.quarkus.crud.rachit.mapper;

import com.quarkus.crud.rachit.dto.DepartmentDto;
import com.quarkus.crud.rachit.entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface DepartmentMapper {
    DepartmentDto toDepartmentDto(Department department);
    List<DepartmentDto> toDepartmentDtoList(List<Department> departments);
    Department toDepartmentEntity(DepartmentDto departmentDto);
}

