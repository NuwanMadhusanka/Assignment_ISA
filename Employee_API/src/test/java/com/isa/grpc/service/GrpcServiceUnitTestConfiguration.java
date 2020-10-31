package com.isa.grpc.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServiceUnitTestConfiguration {

//    @Bean
//    OtherDependency foobar() {
//        // return mock(OtherDependency.class);
//    }

    @Bean
    GrpcService myService() {
        return new GrpcService();
    }
}
