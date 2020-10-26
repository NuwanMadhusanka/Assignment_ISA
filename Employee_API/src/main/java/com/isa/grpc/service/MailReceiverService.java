package com.isa.grpc.service;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class MailReceiverService {

    @Bean
    @ServiceActivator(inputChannel = "pop3Channel")
    public MessageHandler processNewEmail() {
        MessageHandler messageHandler = new MessageHandler() {

            @Override
            public void handleMessage(org.springframework.messaging.Message<?> message) throws MessagingException {
                System.out.println("New email:" + message.getHeaders().toString());
                System.out.println(message.getPayload());


                Object payload = message.getPayload();
                MimeMessage msg = (MimeMessage) payload;
                try {
                    //System.out.println(msg.getFolder().getMessage(0));
                    MimeMessage msg1 = (MimeMessage) payload;
                    System.out.println(String.format("Headers [%s] Subject [%s]. Content-Type [%s].", msg.getAllHeaders(),
                            msg1.getSubject(), msg1.getContentType()));

                    System.out.println("----------");
                    handleMessage1(msg1);
                } catch (javax.mail.MessagingException | IOException e) {
                    e.printStackTrace();
                }


                //Processing emails do with them something..
//                for (EmailAction emailAction : services) {
//                    emailAction.performAction(null);
//                }

            }
        };
        return messageHandler;
    }

    private void handleMessage1(MimeMessage msg) throws MessagingException, IOException, javax.mail.MessagingException {

//        if (msg.getContentType().contains(MediaType.TEXT_PLAIN_VALUE)) {
//            System.out.println((String) msg.getContent());
//        }

        Multipart mime = (Multipart) msg.getContent();

        String content="";
        for (int i = 0; i < mime.getCount(); i++)
        {
            BodyPart part = mime.getBodyPart(i);
            content += part.getContent().toString();
        }
        System.out.println(content);

    }

}
