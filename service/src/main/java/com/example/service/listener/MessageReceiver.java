package com.example.service.listener;

import com.example.service.service.TeamAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    private TeamAssignmentService teamAssignmentService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(Notification notification) {
        logger.info("Batch job done at: " + notification.getTimestamp());
        teamAssignmentService.assignTask();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}
}
