package com.palms.animal.game.repository;

import com.palms.animal.game.domain.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Reporitory for Property.
 */
@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {

}
