package com.palms.animal.game.repository;

import com.palms.animal.game.domain.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for animal properties.
 */
@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {

   /**
    * Find property by its name.
    *
    * @param name property name
    * @return {@link Property}
    */
   Property findPropertyByName(String name);
}
