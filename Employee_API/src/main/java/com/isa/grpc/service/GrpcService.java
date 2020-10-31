package com.isa.grpc.service;

import com.isa.grpc.protobuf.Employee;
import com.isa.grpc.protobuf.EmployeeServiceGrpc;
import com.isa.grpc.repository.EmployeeRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

@net.devh.boot.grpc.server.service.GrpcService
public class GrpcService extends EmployeeServiceGrpc.EmployeeServiceImplBase{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void getEmployeeData(
            Employee.EmployeeRequest request,
            StreamObserver<Employee.EmployeeResponse> responseObserver) {

        String employeeEmail = request.getEmail().trim();

        if(!employeeEmail.isEmpty()){
            Employee.EmployeeResponse response;
            com.isa.grpc.entity.Employee employee = employeeRepository.findByEmployeeEmail(employeeEmail);

            if(employee!=null){
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

            }else{
                responseObserver.onError(Status.INTERNAL.withDescription("Employee's data do not exist in the system.").asException());
            }

        }else{
            responseObserver.onError(Status.INTERNAL.withDescription("Email cannot be empty").asException());
        }
        responseObserver.onCompleted();
    }
}
