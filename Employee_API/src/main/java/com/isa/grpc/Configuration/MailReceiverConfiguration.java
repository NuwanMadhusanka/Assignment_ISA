package com.isa.grpc.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.mail.internet.MimeMessage;

@Configuration
public class MailReceiverConfiguration {

    @Bean
    public HeaderMapper<MimeMessage> mailHeaderMapper() {
        return new DefaultMailHeaderMapper();
    }


    @Bean
    public IntegrationFlow pop3MailFlow() {
        return IntegrationFlows
                .from(Mail.pop3InboundAdapter("pop3s://drivolearners%40gmail.com:ucsc123!@pop.gmail.com:995/INBOX")
                                .userFlag("testSIUserFlag")
                                .simpleContent(true)
                                .javaMailProperties(p -> p.put("mail.debug", "false")),
                        e -> e.autoStartup(true)
                                .poller(p -> p.fixedDelay(1000)))//4 Sec
                .channel(MessageChannels.queue("pop3Channel"))
                .get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
        return pollerMetadata;
    }
}


