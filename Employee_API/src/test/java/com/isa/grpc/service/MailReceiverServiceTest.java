package com.isa.grpc.service;

import com.isa.grpc.EmployeeApiApplication;
import com.isa.grpc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.Date;
import java.util.Properties;

@SpringBootTest(classes = EmployeeApiApplication.class)
public class MailReceiverServiceTest extends org.springframework.test.context.testng.AbstractTestNGSpringContextTests {

    @Autowired
    MailReceiverService mailReceiverService;

    @MockBean
    EmployeeRepository employeeRepository;


    @Test(dataProvider = "EmailDataProvider")
    void testHandleText(String emailContent) throws MessagingException, IOException {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(mailProps, null);
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-Type", "multipart");
        msg.setFrom(new InternetAddress("sender@here.com"));
        msg.setSubject("Test");
        msg.setSentDate(new Date());
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("receiver@there.com"));

        MimeBodyPart bodyPart1 = new MimeBodyPart();
        bodyPart1.setHeader("Content-Type", "text/html; charset=utf-8");
        bodyPart1.setContent(emailContent, "text/html");
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart1, 0);
        msg.setContent(multiPart, "multipart");
        msg.saveChanges();

        //Mockito.when(mailReceiverService.employeeSave(new String[]{""},"")).thenReturn("success");
        String result = mailReceiverService.handleMsgContent(msg);
        Assert.assertEquals(result,"success");
    }


    @Test(dataProvider = "EmployeeDataProvider")
    void testEmployeeSaveMethod(String[] employeeData, String email) {
        String result = mailReceiverService.employeeSave(employeeData, email);
        Assert.assertEquals(result, "success");
    }

    @DataProvider(name = "EmployeeDataProvider")
    public Object[][] getEmployeeData() {
        Object[][] data = {
                {new String[]{"", "", "", "", "", "", ""}, ""},//Invalid all
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1s", "2020/10/10", "0773015590"}, "nuwan@gmail.com"},//Invalid empId
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "202010/10", "0773015590"}, "nuwan@gmail.com"},//Invalid joinDate
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", "07305590"}, "nuwan@gmail.com"},//Invalid Mobile
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "202010/10", "073015590"}, "nuwan@gmail.com"},//Invalid joinDate and Mobile
                {new String[]{"Nuwan", " ", "Dev", "aeroMART", "1", "2020/10/10", "073015590"}, "nuwan@gmail.com"},//Missing lastName
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", "0773015591"}, " "},//Missing Email
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", " "}, "nuwan@gmail.com"},//Missing Mobile
                {new String[]{" ", " ", " ", "aeroMART", "1", "2020/10/10", "073015590"}, "nuwan@gmail.com"},//Missing few fields
                {new String[]{" ", " ", " ", "aeroMART", "1", "2020/10/10", "073015590"}, ""},//Missing few fields
                {new String[]{"Nuwan", "Madhusanka", "Dev", "aeroMART", "1", "2020/10/10", "073015590"}, "nuwan@gmail.com"},//Valid
        };
        return data;
    }

    @DataProvider(name = "EmailDataProvider")
    public Object[][] getEmailData(){
        Object[][] data = {
                {" "},
                {"Nuwan Madusanka Dev aeroMart"},
                {", , , , , , ,"},
                {"Nuwan Madusanka DEV aeroConnect 132 2020/10/20 0779532429"}
        };
        return data;
    }
}
