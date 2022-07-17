package com.quarkus.crud.rachit.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Employee extends PanacheEntity {

    public String first_name;
    public String last_name;
    public String gender;
    public LocalDate birth_date;
    public LocalDate hire_date;

    @ManyToOne(fetch = FetchType.LAZY)
    public Department department;

    public static List<Employee> findEmployeesByDepartmentId(Long departmentId) {
        return find("department.id", departmentId).list();
    }

    public static List<Employee> searchEmpsByName(String name) {

        // return find("from Employee e WHERE (0 < LOCATE(?1,e.first_name)) ", name).list();
        return find("first_name like CONCAT('%',?1, '%') ", name).list();
    }


}
