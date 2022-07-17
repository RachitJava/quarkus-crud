package com.quarkus.crud.rachit.service;

import com.quarkus.crud.rachit.dto.EmployeeDto;
import com.quarkus.crud.rachit.entity.Department;
import com.quarkus.crud.rachit.entity.Employee;
import com.quarkus.crud.rachit.mapper.EmployeeMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeMapper employeeMapper;

    public EmployeeDto getEmployee(Long id) {
        return employeeMapper.toEmployeeDto(Employee.findById(id));
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeMapper.toEmployeeList(Employee.findAll().list());
    }

    public List<EmployeeDto> getEmployeesByDepartment(Long deptId) {
        return employeeMapper.toEmployeeList(Employee.findEmployeesByDepartmentId(deptId));
    }

    public List<EmployeeDto> searchEmpsByName(String name) {
        return employeeMapper.toEmployeeList(Employee.searchEmpsByName(name));
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employee) {
        Employee entity = employeeMapper.toEmployeeEntity(employee);
        Employee.persist(entity);
        entity.persistAndFlush();
        if(entity.isPersistent()) {
            Optional<Employee> optionalEmp = Employee.findByIdOptional(entity.id);
            entity = optionalEmp.orElseThrow(NotFoundException::new);
            return employeeMapper.toEmployeeDto(entity);
        } else {
            throw new PersistenceException();
        }

    }

    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto employee) {
        Employee entity  = Employee.findById(id);
        if(entity == null) {
            throw new WebApplicationException("Employee with id of " + id + " does not exist.", 404);
        }
        employeeMapper.updateEmployeeEntityFromDto(employee,entity);
        return employeeMapper.toEmployeeDto(entity);
    }

    @Transactional
    public EmployeeDto updateEmployee(EmployeeDto employee) {
        Employee entity  = Employee.findById(employee.getId());
        if(entity == null) {
            throw new WebApplicationException("Employee with id " + employee.getId() + " does not exist.", 404);
        }
        employeeMapper.updateEmployeeEntityFromDto(employee,entity);
        entity =  Employee.getEntityManager().merge(entity);
        return employeeMapper.toEmployeeDto(entity);
    }

    @Transactional
    public EmployeeDto updateEmpDepartment(Long empId, Department department) {
        Employee entity  = Employee.findById(empId);
        if(entity == null) {
            throw new WebApplicationException("Employee with id " + empId + " does not exist.", 404);
        }
        entity.department = department;
        return employeeMapper.toEmployeeDto(entity);
    }

    @Transactional
    public EmployeeDto updateEmpDepartment(Long empId, Long deptId) {
        Employee entity  = Employee.findById(empId);
        if(entity == null) {
            throw new WebApplicationException("Employee with id " + empId + " does not exist.", 404);
        }
        Optional<Department> department = Department.findByIdOptional(deptId);
        if(department.isPresent()) {
            entity.department = department.get();
        }
        return employeeMapper.toEmployeeDto(entity);
    }


    @Transactional
    public Response deleteEmployee(Long id) {
        boolean isEntityDeleted = Employee.deleteById(id);
        if(!isEntityDeleted) {
            throw new WebApplicationException("Employee with id of " + id + " does not exist.", 404);
        }
        return Response.status(200).build();

    }
}
