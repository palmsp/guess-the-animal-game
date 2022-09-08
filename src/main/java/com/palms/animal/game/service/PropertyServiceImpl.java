package com.palms.animal.game.service;

import com.palms.animal.game.domain.Property;
import com.palms.animal.game.repository.PropertyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
@AllArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private PropertyRepository propertyRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getProperties() {
        return propertyRepository.findAll().stream()
                .map(Property::getName)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public void saveProperty(String property) {
        log.info("Saving property:{}", property);
        propertyRepository.save(Property.builder().name(property).build());
    }
}
