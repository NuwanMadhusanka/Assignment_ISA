package com.isa.grpc.service;


import com.isa.grpc.EmployeeApiApplication;
import com.isa.grpc.protobuf.Employee;
import com.isa.grpc.repository.EmployeeRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = EmployeeApiApplication.class)
public class GrpcServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GrpcService grpcService;

    @MockBean
    @Autowired
    EmployeeRepository employeeRepository;

    @Test(dataProvider = "requestEmailData")
    void testGetEmployeeData(String requestEmail, Integer flag) throws Exception {

        com.isa.grpc.entity.Employee employee = new com.isa.grpc.entity.Employee();
        employee.setEmployeeId(1);
        employee.setFirstName("Nuwan");
        employee.setLastName("Madhusanka");
        employee.setDepartment("Dev");
        employee.setTeam("aeroMART");
        employee.setMobile("0779056696");
        employee.setJoinDate(LocalDate.of(2020,10,12));
        employee.setEmail("nuwan@gmail.com");
        Mockito.when(employeeRepository.findByEmployeeEmail("nuwan@gmail.com")).thenReturn(employee);

        Employee.EmployeeRequest request = Employee.EmployeeRequest.newBuilder().setEmail(requestEmail).build();
        StreamRecorder<Employee.EmployeeResponse> responseObserver = StreamRecorder.create();
        grpcService.getEmployeeData(request,responseObserver);
        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            System.out.println("Fail");
        }


        if(flag==1){
            Assert.assertNull(responseObserver.getError());
            List<Employee.EmployeeResponse> results = responseObserver.getValues();
            Assert.assertEquals(results.size(),1);
            Assert.assertEquals(results.get(0).getEmployeeId(),1);
            Assert.assertEquals(results.get(0).getFirstName(),"Nuwan");
            Assert.assertEquals(results.get(0).getLastName(),"Madhusanka");
            Assert.assertEquals(results.get(0).getDepartment(),"Dev");
            Assert.assertEquals(results.get(0).getTeam(),"aeroMART");
            Assert.assertEquals(results.get(0).getJoinDate(),"2020-10-12");
            Assert.assertEquals(results.get(0).getMobile(),"0779056696");
        }else if(flag==2){
            Assert.assertNotNull(responseObserver.getError());
            Assert.assertEquals(responseObserver.getError().getMessage(),"INTERNAL: Employee's data do not exist in the system.");
        }else{
            Assert.assertNotNull(responseObserver.getError());
            Assert.assertEquals(responseObserver.getError().getMessage(),"INTERNAL: Email cannot be empty");
        }


    }

    @DataProvider(name = "requestEmailData")
    public Object[][] getEmployeeData2() {
        Object[][] data = {
                {" nuwan@gmail.com",1},//Exist email in db
                {"asa@gmail.com",2},//Not exist email in db
                {" ",3}
        };
        return data;
    }
}
