package com.palms.animal.game.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for accessing REST api
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {

    public static final String API_V1 = "/api/v1";

    public static final String ANIMAL_GAME = API_V1 + "/animal-game";

    public static final String PROPERTIES = "/properties";
    public static final String GUESSED_ANIMAL = "/guessedAnimal";
    public static final String REAL_ANIMAL = "/realAnimal";
    public static final String DIFFERENCE = "/difference";
}
