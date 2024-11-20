package portal.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import portal.base.dto.PortalUserDTO;
import portal.config.ApplicationConfig;
import portal.config.ApplicationConfig.PortalProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class BaseService {
	
	@Value("${portal.version}")
	private String version;
	
	private final ApplicationConfig applicationConfig;
	private final BaseRepository baseRepository;
	
	public PortalProperties getPortalProperties() {
		return applicationConfig.portalProperties();
	}

	public List<PortalUserDTO> findAllUser() {
		return baseRepository.findAllUser();
	}
	
	public PortalUserDTO getPortalUser(String userId) {
		return baseRepository.findByUserId(userId);
	}


//	@Value("${activemq.queue.name}")
//    private String queueName;
//
//    // jmsTemplate 을 통해 메세지 송신 가능
//    private final JmsTemplate jmsTemplate;
//
//    /**
//     * Queue 로 메세지를 발행
//     * messageDto -> Producer 가 Queue 발행한 메세지 Class
//     */
//    public void sendMessage(MessageDto messageDto) {
//        log.info("message sent : {}", messageDto.toString());
//        // queueName(Sample-queue) 에 메세지 전송
//        jmsTemplate.convertAndSend(queueName,messageDto);
//    }
//
//    @JmsListener(destination = "${activemq.queue.name}")
//    public void receiveMessage(MessageDto messageDto) {
//        log.info("Received message : {}",messageDto.toString());
//    }

	
}
