package edu.dosw.sirha.sirha_backend.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;

public interface BaseRequestMongoRepository extends MongoRepository<BaseRequest, String> {

    List<BaseRequest> findByActualState(RequestStateEnum status);
}
