package edu.dosw.sirha.SIRHA_BackEnd.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;

public interface RequestMongoRepository extends MongoRepository<BaseRequest, String> {
}
