package com.isa.grpc.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class EmployeeTest {

    private Validator validator;

    @BeforeClass
    public void setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test(dataProvider = "EmployeeDataProvider1")
    void employeeValidationTestNotSuccess(Integer empId, String firstName, String lastName, String department, String team, LocalDate joinDate, String mobile, String email){

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setTeam(team);
        employee.setJoinDate(joinDate);
        employee.setMobile(mobile);
        employee.setEmail(email);

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        for (ConstraintViolation e: violations) {
            System.out.print(e.getMessage()+ " ,");
        }
        Assert.assertFalse(violations.isEmpty(),"Constrain violation not happen in Employee object");
    }

    @Test(dataProvider = "EmployeeDataProvider2")
    void employeeValidationTestSuccess(Integer empId, String firstName, String lastName, String department, String team, LocalDate joinDate, String mobile, String email){

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setTeam(team);
        employee.setJoinDate(joinDate);
        employee.setMobile(mobile);
        employee.setEmail(email);

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        for (ConstraintViolation e: violations) {
            System.out.print(e.getMessage()+ " ,");
        }
        Assert.assertTrue(violations.isEmpty(),"Constrain violation happen in Employee object");
    }

    @DataProvider(name = "EmployeeDataProvider1")
    public Object[][] getData1(){
        Object[][] data = {
                {null,""," "," "," ",null," "," "},
                {-1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com"},//Invalid empId
                {1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 11, 8),"0773015590","nuwan@gmail.com"},//Invalid date
                {1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 1, 8),"077301590","nuwan@gmail.com"},//Invalid Mobile
                {1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 1, 8),"0773015590","nuwangmail.com"},//Invalid Email
                {1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 1, 8),"0773015590"," "},//Blank email
        };
        return data;
    }

    @DataProvider(name = "EmployeeDataProvider2")
    public Object[][] getData2(){
        Object[][] data = {
                {1,"Nuwan","Madhusanka","Dev","aeroMART",LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com"}//all valid
        };
        return data;
    }
}
