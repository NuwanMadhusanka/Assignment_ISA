package com.isa.grpc.service;

import com.isa.grpc.entity.Employee;
import com.isa.grpc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MailReceiverService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Bean
    @ServiceActivator(inputChannel = "pop3Channel")
    public MessageHandler processNewEmail() {
        MessageHandler messageHandler = new MessageHandler() {

            @Override
            public void handleMessage(org.springframework.messaging.Message<?> message) throws MessagingException {

                Object payload = message.getPayload();
                MimeMessage msg = (MimeMessage) payload;

                try {
                    handleText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            }
        };
        return messageHandler;
    }

    private void handleText(MimeMessage msg) throws MessagingException, IOException, javax.mail.MessagingException {


        Multipart mime = (Multipart) msg.getContent();
        String content = "";

        for (int i = 0; i < mime.getCount(); i++) {

            BodyPart part = mime.getBodyPart(i);
            content = part.getContent().toString().trim();
            String[] employeeData = content.split(" ");

            if (employeeData.length == 7) {//Save employee data

                //Get Employee Email
                Address[] from = msg.getFrom();
                String email = from == null ? null : ((InternetAddress) from[0]).getAddress();

                employeeSave(employeeData, email);
                break;
            }
        }

    }

    public String employeeSave(String[] employeeData, String email) {

        String firstName = employeeData[0].trim();
        String lastName = employeeData[1].trim();
        String department = employeeData[2].trim();
        String team = employeeData[3].trim();

        //Get Employee's ID
        Integer employeeId = 0;
        try {
            employeeId = Integer.parseInt(employeeData[4].trim());

            //Get Employee join data
            LocalDate joinDate = LocalDate.of(2020, 1, 8);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");
            try {
                joinDate = LocalDate.parse(employeeData[5].trim(), formatter);

                //Get Employee Mobile Number
                String mobile = employeeData[6].trim();
                if (mobile.length() == 10 && mobile.matches("[0-9]+")) {
                    //Save Employee data to db
                    Employee employee = new Employee();
                    employee.setEmployeeId(employeeId);
                    employee.setFirstName(firstName);
                    employee.setLastName(lastName);
                    employee.setDepartment(department);
                    employee.setTeam(team);
                    employee.setMobile(mobile);
                    employee.setJoinDate(joinDate);
                    employee.setEmail(email);

                    System.out.println(employee);
                    employeeRepository.save(employee);

                    return "success";
                }

            } catch (Exception e) {
                System.out.println("Unable to convert String to Date");
            }

        } catch (NumberFormatException e) {
            System.out.println("Employee's id should be number");
        }

        return "notSuccess";
    }


}
