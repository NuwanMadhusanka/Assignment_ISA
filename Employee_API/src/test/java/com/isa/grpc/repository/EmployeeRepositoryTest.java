package com.isa.grpc.repository;

import com.isa.grpc.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;


@DataJpaTest
public class EmployeeRepositoryTest extends org.springframework.test.context.testng.AbstractTestNGSpringContextTests{


    @Autowired
    private EmployeeRepository employeeRepository;


    @Test
    void testFindEmployeeByEmail() {

        Employee employee = new Employee(1,"Nuwan","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com");
        employeeRepository.save(employee);
        Employee employee2 = employeeRepository.findByEmployeeEmail("nuwan@gmail.com");

        Assert.assertNotNull(employee);
        Assert.assertEquals(employee2.getEmployeeId(), employee.getEmployeeId());
        Assert.assertEquals(employee2.getFirstName(), employee.getFirstName());
        Assert.assertEquals(employee2.getLastName(), employee.getLastName());
        Assert.assertEquals(employee2.getDepartment(), employee.getDepartment());
        Assert.assertEquals(employee2.getTeam(), employee.getTeam());
        Assert.assertEquals(employee2.getJoinDate(), employee.getJoinDate());
        Assert.assertEquals(employee2.getMobile(), employee.getMobile());
        Assert.assertEquals(employee2.getEmail(), employee.getEmail());

    }

    @Test
    void testFindEmployeeByEmployeeId() {

        Employee employee = new Employee(1,"Nuwan","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com");
        employeeRepository.save(employee);
        Employee employee2 = employeeRepository.findByEmployeeId(1);

        Assert.assertNotNull(employee);
        Assert.assertEquals(employee2.getEmployeeId(), employee.getEmployeeId());
        Assert.assertEquals(employee2.getFirstName(), employee.getFirstName());
        Assert.assertEquals(employee2.getLastName(), employee.getLastName());
        Assert.assertEquals(employee2.getDepartment(), employee.getDepartment());
        Assert.assertEquals(employee2.getTeam(), employee.getTeam());
        Assert.assertEquals(employee2.getJoinDate(), employee.getJoinDate());
        Assert.assertEquals(employee2.getMobile(), employee.getMobile());
        Assert.assertEquals(employee2.getEmail(), employee.getEmail());

    }

    @Test(expectedExceptions = {DataIntegrityViolationException.class})
    void testUniqueEmail(){
        Employee employee1 = new Employee(1,"Nuwan","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com");
        employeeRepository.save(employee1);

        Employee employee2 = new Employee(2,"Asanka","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 10, 8),"0713015590","nuwan@gmail.com");
        employeeRepository.save(employee2);
    }

    @Test(expectedExceptions = {DataIntegrityViolationException.class})
    void testUniqueMobile(){
        Employee employee1 = new Employee(1,"Nuwan","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 1, 8),"0773015590","nuwan1@gmail.com");
        employeeRepository.save(employee1);

        Employee employee2 = new Employee(2,"Asanka","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 10, 8),"0773015590","nuwan2@gmail.com");
        employeeRepository.save(employee2);
    }

}
