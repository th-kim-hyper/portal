//package portal.config;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQQueue;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.jms.support.converter.MessageType;
//
//import jakarta.jms.Queue;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import portal.base.dto.MessageDto;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class ActiveMqConfig {
//
//    @Value("${activemq.queue.name}")
//    private String queueName;
//
//    // spring.activeMq prefix 의 설정값을 가지는 클래스
//    private final ActiveMQProperties activeMQProperties;
//
//    /**
//     * 지정된 queue 이름으로 Queue 빈을 생성
//    **/
//    @Bean
//    public Queue queue() {
//        log.info(activeMQProperties.getBrokerUrl());
//        log.info(activeMQProperties.getUser());
//        log.info(activeMQProperties.getPassword());
//        return new ActiveMQQueue(queueName);
//    }
//
//    /**
//     * activeMQ 는  61616 포트로 구동 중이다.
//     * Spring application 에서 해당 서버로 접근해야 한다. ActiveConnectionFactory 로 연결
//     */
//
//    @Bean
//    public ActiveMQConnectionFactory activeMQConnectionFactory() {
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
//        activeMQConnectionFactory.setBrokerURL(activeMQProperties.getBrokerUrl());
//        activeMQConnectionFactory.setUserName(activeMQProperties.getUser());
//        activeMQConnectionFactory.setPassword(activeMQProperties.getPassword());
//        return activeMQConnectionFactory;
//    }
//
//    /**
//     * JmsTemplate 은 연결 후 실제 작업을 하기 위한 template
//     */
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
//        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
//        jmsTemplate.setExplicitQosEnabled(true);    // 메시지 전송 시 QOS을 설정
//        jmsTemplate.setDeliveryPersistent(false);   // 메시지의 영속성을 설정
//        jmsTemplate.setReceiveTimeout(1000 * 3);    // 메시지를 수신하는 동안의 대기 시간을 설정(3초)
//        jmsTemplate.setTimeToLive(1000 * 60 * 30);  // 메시지의 유효 기간을 설정(30분)
//        return jmsTemplate;
//    }
//
//    /**
//     * JmsListenerContainerFactory 을 위한 빈을 생성
//     */
//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(activeMQConnectionFactory());
//        factory.setMessageConverter(jacksonJmsMessageConverter());
//        return factory;
//    }
//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setTypeIdPropertyName("_typeId");
//        Map<String, Class<?>> typeIdMappings = new HashMap<>();
//        typeIdMappings.put("message", MessageDto.class);
//        converter.setTypeIdMappings(typeIdMappings);
//        return converter;
//    }
//}
