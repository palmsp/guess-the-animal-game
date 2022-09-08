package com.palms.animal.game.repository;

import com.palms.animal.game.domain.Animal;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Reporitory for Animal.
 */
@Repository
public interface AnimalRepository extends MongoRepository<Animal, String> {

    /**
     * Returns animal by name.
     *
     * @param name name
     * @return {@link Animal}
     */
    Animal findAnimalByName(String name);

    /**
     * Returns list of animal by property.
     *
     * @param property property
     * @return list of {@link Animal}
     */
    List<Animal> findAnimalsByPropertiesContaining(String property);

    /**
     * Returns list of animal which satisfy properties conditions.
     *
     * @param properties typical properties
     * @param nonTypicalProperties non typical properties
     * @return list of {@link Animal}
     */
    @Query(value = "{ 'properties' : {$all : ?0, $nin : ?1}}")
    List<Animal> findAnimalsWithTheseProperties(List<String> properties, List<String> nonTypicalProperties);

    /**
     * Returns list of animal which satisfy properties conditions.
     *
     * @param properties typical properties
     * @return list of {@link Animal}
     */
    @Query(value = "{ 'properties' : {$all : ?0}}")
    List<Animal> findAnimalsWithTypicalProperties(List<String> properties);

    /**
     * Returns list of animal which satisfy properties conditions.
     *
     * @param nonTypicalProperties not typical properties
     * @return list of {@link Animal}
     */
    @Query(value = "{ 'properties' : {$nin : ?0}}")
    List<Animal> findAnimalsWithNonTypicalProperties(List<String> nonTypicalProperties);
}
