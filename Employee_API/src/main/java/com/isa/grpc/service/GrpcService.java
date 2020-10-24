package com.isa.grpc.service;

import com.isa.grpc.protobuf.Employee;
import com.isa.grpc.protobuf.EmployeeServiceGrpc;
import com.isa.grpc.repository.EmployeeRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

@net.devh.boot.grpc.server.service.GrpcService
public class GrpcService extends EmployeeServiceGrpc.EmployeeServiceImplBase{

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public void getEmployeeData(
            Employee.EmployeeRequest request,
            StreamObserver<Employee.EmployeeResponse> responseObserver) {

        String employeeEmail = request.getEmail();
        System.out.println("--------------- "+employeeEmail);

        Employee.EmployeeResponse response;
        com.isa.grpc.entity.Employee employee = employeeRepository.findByEmployeeEmail(employeeEmail);
        response =
                Employee.EmployeeResponse.newBuilder()
                        .setFirstName(employee.getFirstName())
                        .setLastName(employee.getLastName())
                        .setEmployeeId(employee.getEmployeeId())
                        .setDepartment(employee.getDepartment())
                        .setJoinDate(employee.getJoinDate().toString())
                        .setMobile(employee.getMobile())
                        .setTeam(employee.getTeam())
                        .build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
