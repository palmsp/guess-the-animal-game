package com.palms.animal.game.service;

import java.util.List;

/**
 * Service for working with properties.
 */
public interface PropertyService {

    /**
     * Returns all properties.
     *
     * @return properties list
     */
    List<String> getProperties();

    /**
     * Save property.
     *
     * @param property property
     */
    void saveProperty(String property);
}
