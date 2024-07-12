package com.connectpublications.repository;

import com.connectpublications.model.dto.AuthorPublicationEmailDto;
import com.connectpublications.model.entity.Publication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PublicationRepository extends CrudRepository<Publication, UUID> {

    @Query("SELECT p FROM Publication p " +
            "JOIN FETCH p.author a " +
            "LEFT JOIN FETCH a.followers " +
            "WHERE p.publicationId = :publicationId")
    Optional<Publication> findByIdWithAuthorAndFollowers(UUID publicationId);

    @Query("SELECT new com.connectpublications.model.dto.AuthorPublicationEmailDto(p.publicationId, u.email) " +
            "FROM Publication p " +
            "JOIN User u ON p.author.userId = u.userId " +
            "WHERE p.publicationId = :publicationId")
    Optional<AuthorPublicationEmailDto> findPublicationAuthorEmail(UUID publicationId);
}
