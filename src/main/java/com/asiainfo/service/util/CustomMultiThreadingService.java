package com.asiainfo.service.util;

import com.asiainfo.model.kafka.KafkaConfigEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

public interface CustomMultiThreadingService {

    public Future<KafkaConfigEntity> getLag(KafkaConfigEntity kafkaConfigEntity);
}
