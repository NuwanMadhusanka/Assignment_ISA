package com.isa.grpc.service;


import com.isa.grpc.protobuf.Employee;
import com.isa.grpc.repository.EmployeeRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
//@SpringJUnitConfig(classes = { GrpcServiceUnitTestConfiguration.class })
public class GrpcServiceTest extends org.springframework.test.context.testng.AbstractTestNGSpringContextTests{

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    @Autowired
    GrpcService grpcService;

    @BeforeClass
    public void create(){
        MockitoAnnotations.initMocks(this);

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
    }

    @Test
    void testGetEmployeeData() throws Exception {

        Employee.EmployeeRequest request = Employee.EmployeeRequest.newBuilder().setEmail("nuwan@gmail.com").build();
        StreamRecorder<Employee.EmployeeResponse> responseObserver = StreamRecorder.create();
        grpcService.getEmployeeData(request,responseObserver);
        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            System.out.println("Fail");
        }
        Assert.assertNull(responseObserver.getError());
        List<Employee.EmployeeResponse> results = responseObserver.getValues();
        Assert.assertEquals(results.size(),7);

        Employee.EmployeeResponse response = results.get(0);
        //Assert.assertEquals();
    }
}
