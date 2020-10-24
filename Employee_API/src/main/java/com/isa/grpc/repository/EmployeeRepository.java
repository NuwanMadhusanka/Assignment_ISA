package com.isa.grpc.repository;

import com.isa.grpc.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    @Query("FROM Employee e WHERE e.employeeId = :employeeId")
    public Employee findByEmployeeId(@Param("employeeId") Integer employeeId);

    @Query("FROM Employee e WHERE e.email = :email")
    public Employee findByEmployeeEmail(@Param("email") String email);
}
