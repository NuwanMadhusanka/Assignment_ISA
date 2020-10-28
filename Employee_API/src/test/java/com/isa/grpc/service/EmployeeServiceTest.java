package com.isa.grpc.service;

import com.isa.grpc.EmployeeApiApplication;
import com.isa.grpc.entity.Employee;
import com.isa.grpc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = EmployeeApiApplication.class)
public class EmployeeServiceTest extends org.springframework.test.context.testng.AbstractTestNGSpringContextTests{

    @Autowired
    MailReceiverService mailReceiverService;

    @Autowired
    @MockBean
    private EmployeeRepository employeeRepository;

    //private Employee employee;



    @Test(dataProvider = "EmployeeDataProvider")
    void testEmployeeSaveMethod(String[] employeeData, String email){
        Employee employee =  employee = new Employee(1,"Nuwan","Madhusanka","Dev","aeroMART", LocalDate.of(2020, 1, 8),"0773015590","nuwan@gmail.com");
        when(employeeRepository.save(employee)).thenReturn(employee);

        String result = mailReceiverService.employeeSave(employeeData,email);
        Assert.assertEquals(result,"success");
    }

    @DataProvider(name = "EmployeeDataProvider")
    public Object[][] getData(){
        Object[][] data = {
                {new String[] {"","","","","","",""},""},//Invalid all
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1s","2020/10/10","0773015590"},"nuwan@gmail.com"},//Invalid empId
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","202010/10","0773015590"},"nuwan@gmail.com"},//Invalid joinDate
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","2020/10/10","07305590"},"nuwan@gmail.com"},//Invalid Mobile
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","202010/10","073015590"},"nuwan@gmail.com"},//Invalid joinDate and Mobile
                {new String[] {"Nuwan"," ","Dev","aeroMART","1","2020/10/10","073015590"},"nuwan@gmail.com"},//Missing lastName
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","2020/10/10","0773015591"}," "},//Missing Email
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","2020/10/10"," "},"nuwan@gmail.com"},//Missing Mobile
                {new String[] {" "," "," ","aeroMART","1","2020/10/10","073015590"},"nuwan@gmail.com"},//Missing few fields
                {new String[] {" "," "," ","aeroMART","1","2020/10/10","073015590"},""},//Missing few fields
                {new String[] {"Nuwan","Madhusanka","Dev","aeroMART","1","2020/10/10","073015590"},"nuwan@gmail.com"},//Valid
        };
        return data;
    }
}
