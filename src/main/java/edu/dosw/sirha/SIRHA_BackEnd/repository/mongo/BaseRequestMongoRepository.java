package edu.dosw.sirha.sirha_backend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;

public interface BaseRequestMongoRepository extends MongoRepository<BaseRequest, String> {
}
