package com.palms.animal.game.service;

import com.palms.animal.game.dto.Assumption;
import com.palms.animal.game.dto.Difference;
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
     * Returns assumption. It can be animal or next property.
     *
     * @param propertyName property name
     * @param isTypical is typical for animal
     */
    Assumption getAssumption(String propertyName, boolean isTypical);

    /**
     * Save properties by difference.
     *
     * @param difference difference
     */
    void saveProperty(Difference difference);
}
