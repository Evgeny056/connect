package com.connectpublications.repository;

import com.connectpublications.model.entity.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PublicationRepository extends CrudRepository<Publication, UUID> {
}
