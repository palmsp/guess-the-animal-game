package com.palms.animal.game.service;

import com.palms.animal.game.domain.Node;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.dto.Assumption;
import com.palms.animal.game.dto.Difference;
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
    public Assumption getAssumption(String propertyName, boolean isTypical) {
        log.info("Start guessing animal by property:{},typical:{}", propertyName, isTypical);
        Property property = propertyRepository.findPropertyByName(propertyName);
        Node node = isTypical ? property.getLeft() : property.getRight();
        Assumption assumption = Assumption.builder()
                .value(node.getValue())
                .isAnimal(node.getIsAnimal())
                .build();
        log.info("Finish guessing animal. Assumption:{},isAnimal:{}", assumption.getValue(), assumption.getIsAnimal());
        return assumption;
    }

    /**
     * {@inheritDoc}
     */
    public void saveProperty(Difference difference) {
        log.info("Start saving property:{}, updating:{}", difference.getNewProperty(), difference.getOldProperty());
        Property property = propertyRepository.findPropertyByName(difference.getOldProperty());
        Node nodeWithNewProperty = Node.builder().value(difference.getNewProperty()).isAnimal(false).build();
        Node nodeWithAnimal;
        if (difference.getIsOldTypical()) {
            nodeWithAnimal = Node.builder().value(property.getLeft().getValue()).isAnimal(true).build();
            property.setLeft(nodeWithNewProperty);
        } else {
            nodeWithAnimal = Node.builder().value(property.getRight().getValue()).isAnimal(true).build();
            property.setRight(nodeWithNewProperty);
        }
        propertyRepository.save(property);
        log.info("Finish updating property:{}", difference.getOldProperty());

        if (!isAlreadyExist(difference.getNewProperty())) {
            Property newProperty = Property.builder()
                    .name(difference.getNewProperty())
                    .left(Node.builder().value(difference.getCorrectAnimal()).isAnimal(true).build())
                    .right(nodeWithAnimal)
                    .build();
            propertyRepository.save(newProperty);
            log.info("Finish saving new property:{}", difference.getNewProperty());
        }
    }

    private boolean isAlreadyExist(String name) {
        Property property = propertyRepository.findPropertyByName(name);
        if (property != null) {
            log.info("New property:{} cannot be saved, its already exist", name);
        }
        return property != null;
    }
}
