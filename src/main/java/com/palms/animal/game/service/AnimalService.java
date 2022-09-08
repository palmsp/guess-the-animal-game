package com.palms.animal.game.service;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.RealAnimal;
import java.util.List;

/**
 * Service for working with animals.
 */
public interface AnimalService {

    /**
     * Return guessed Animal.
     *
     * @param properties list of {@link AnimalProperty}
     * @return {@link Animal}
     */
    Animal fetchGuessedAnimal(List<AnimalProperty> properties);

    /**
     * Save or update real animal.
     *
     * @param realAnimal {@link RealAnimal}
     */
    void saveAnimal(RealAnimal realAnimal);
}
