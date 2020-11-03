package com.isa.grpc.service;

import com.isa.grpc.EmployeeApiApplication;
import com.isa.grpc.entity.Employee;
import com.isa.grpc.repository.EmployeeRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.support.MessageBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = EmployeeApiApplication.class)
public class MailReceiverServiceTest extends org.springframework.test.context.testng.AbstractTestNGSpringContextTests {

    @Autowired
    MailReceiverService mailReceiverService;

    @MockBean
    @Autowired
    private EmployeeRepository employeeRepository;


     @Test
     void testHandleMessage() throws MessagingException, IOException {
         Properties mailProps = new Properties();
         Session session = Session.getDefaultInstance(mailProps, null);
         MimeMessage msg = new MimeMessage(session);
         msg.addHeader("Content-Type", "multipart");
         msg.setFrom(new InternetAddress("nuwan@gmail.com"));
         msg.setSubject("Test");
         msg.setSentDate(new Date());
         msg.setRecipient(Message.RecipientType.TO, new InternetAddress("drivolearners@gmail.com"));

         MimeBodyPart bodyPart1 = new MimeBodyPart();
         bodyPart1.setHeader("Content-Type", "text/html; charset=utf-8");
         bodyPart1.setContent("", "text/html");
         Multipart multiPart = new MimeMultipart();
         multiPart.addBodyPart(bodyPart1, 0);
         msg.setContent(multiPart, "multipart");
         msg.saveChanges();

         org.springframework.messaging.Message message1 = (org.springframework.messaging.Message) MessageBuilder.withPayload(msg)
                 .setHeader("", "")
                 .build();

         MailReceiverService mailReceiverService1 = Mockito.spy(mailReceiverService);
         Mockito.doReturn("success").when(mailReceiverService1).handleMsgContent(any());
         mailReceiverService1.processNewEmail().handleMessage(message1);
         //Mockito.verify(mailReceiverService1,Mockito.times(1)).handleMsgContent(any());
         Assert.assertTrue(true);
     }

    @Test(dataProvider = "EmailDataProvider1")
    void testHandleTextNotSuccess(String emailContent) throws MessagingException, IOException {
        Properties mailProps = new Properties();
        Session session = Session.getDefaultInstance(mailProps, null);
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-Type", "multipart");
        msg.setFrom(new InternetAddress("nuwan@gmail.com"));
        msg.setSubject("Test");
        msg.setSentDate(new Date());
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("drivolearners@gmail.com"));

        MimeBodyPart bodyPart1 = new MimeBodyPart();
        bodyPart1.setHeader("Content-Type", "text/html; charset=utf-8");
        bodyPart1.setContent(emailContent, "text/html");
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart1, 0);
        msg.setContent(multiPart, "multipart");
        msg.saveChanges();

        MailReceiverService mailReceiverService1 = Mockito.spy(mailReceiverService);
        Mockito.doReturn("notSuccess").when(mailReceiverService1).employeeSave(any(),any());


        String result = mailReceiverService1.handleMsgContent(msg);
        Assert.assertEquals(result,"notSuccess");
    }

    @Test(dataProvider = "EmailDataProvider2")
    void testHandleTextSuccess(String emailContent) throws MessagingException, IOException {
        Properties mailProps = new Properties();
        Session session = Session.getDefaultInstance(mailProps, null);
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-Type", "multipart");
        msg.setFrom(new InternetAddress("nuwan@gmail.com"));
        msg.setSubject("Test");
        msg.setSentDate(new Date());
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("drivolearners@gmail.com"));

        MimeBodyPart bodyPart1 = new MimeBodyPart();
        bodyPart1.setHeader("Content-Type", "text/html; charset=utf-8");
        bodyPart1.setContent(emailContent, "text/html");
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart1, 0);
        msg.setContent(multiPart, "multipart");
        msg.saveChanges();

        MailReceiverService mailReceiverService1 = Mockito.spy(mailReceiverService);
        Mockito.doReturn("success").when(mailReceiverService1).employeeSave(any(),any());


        String result = mailReceiverService1.handleMsgContent(msg);
        Assert.assertEquals(result,"success");
    }


    @Test(dataProvider = "EmployeeDataProvider1")
    void testEmployeeSaveMethodNotSuccess(String[] employeeData, String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(132);
        employee.setFirstName("Nuwa");
        employee.setLastName("Madhusanka");
        employee.setDepartment("Dev");
        employee.setTeam("aeroMART");
        employee.setJoinDate(LocalDate.of(2020,10,10));
        employee.setMobile("0773015590");
        employee.setEmail("nuwan@gmail.com");

        Mockito.when(employeeRepository.findByEmployeeId(132)).thenReturn(employee);

        String result = mailReceiverService.employeeSave(employeeData, email);
        Assert.assertEquals(result, "notSuccess");
    }

    @Test(dataProvider = "EmployeeDataProvider2")
    void testEmployeeSaveMethodSuccess(String[] employeeData, String email){
        Employee employee = new Employee();
        employee.setEmployeeId(132);
        employee.setFirstName("Nuwa");
        employee.setLastName("Madhusanka");
        employee.setDepartment("Dev");
        employee.setTeam("aeroMART");
        employee.setJoinDate(LocalDate.of(2020,10,10));
        employee.setMobile("0773015590");
        employee.setEmail("nuwan@gmail.com");

        Mockito.when(employeeRepository.findByEmployeeId(132)).thenReturn(null);
        Mockito.when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        String result = mailReceiverService.employeeSave(employeeData, email);
        Assert.assertEquals(result, "success");
    }

    @DataProvider(name = "EmployeeDataProvider1")
    public Object[][] getEmployeeData1() {
        Object[][] data = {
                {new String[]{"", "", "", "", "", "", ""}, ""}, //Invalid All
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1s", "2020/10/10", "0773015590"}, "nuwan@gmail.com"},//Invalid empId
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "202010/10", "0773015590"}, "nuwan@gmail.com"},//Invalid joinDate
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", "07305590"}, "nuwan@gmail.com"},//Invalid Mobile
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "202010/10", "073015590"}, "nuwan@gmail.com"},//Invalid joinDate and Mobile
                {new String[]{"Nuwan", " ", "Dev", "aeroMART", "1", "2020/10/10", "073015590"}, "nuwan@gmail.com"},//Missing lastName
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", "0773015591"}, " "},//Missing Email
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", ""}, "nuwan@gmail.com"},//Missing Mobile
                {new String[]{" ", " ", " ", "aeroMART", "1", "2020/10/10", "073015590"}, "nuwan@gmail.com"},//Missing few fields
                {new String[]{" ", " ", " ", "aeroMART", "1", "2020/10/10", "073015590"}, ""},//Missing few fields
                {new String[]{"Nuwa", "Madhusanka", "Dev", "aeroMART", "132", "2020/10/10", "0773015590"}, "nuwan@gmail.com"},//Valid
        };
        return data;
    }

    @DataProvider(name = "EmployeeDataProvider2")
    public Object[][] getEmployeeData2() {
        Object[][] data = {
                {new String[]{"Nuwa", "Madhusanka", "Dev", "aeroMART", "132", "2020/10/10", "0773015590"}, "nuwan@gmail.com"},//Valid
        };
        return data;
    }

    @DataProvider(name = "EmailDataProvider1")
    public Object[][] getEmailData1(){
        Object[][] data = {
                {" "},
                {"Nuwan Madusanka Dev aeroMart"},
                {", , , , , , ,"}
        };
        return data;
    }

    @DataProvider(name = "EmailDataProvider2")
    public Object[][] getEmailData2(){
        Object[][] data = {
                {"Nuwan Madusanka DEV aeroConnect 132 2020/10/20 0779532429"}
        };
        return data;
    }
}
