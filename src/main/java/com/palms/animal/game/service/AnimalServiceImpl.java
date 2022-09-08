package com.palms.animal.game.service;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.RealAnimal;
import com.palms.animal.game.repository.AnimalRepository;
import java.util.ArrayList;
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
public class AnimalServiceImpl implements AnimalService {

    private AnimalRepository animalRepository;

    /**
     * {@inheritDoc}
     */
    public Animal fetchGuessedAnimal(List<AnimalProperty> properties) {
        log.info("Start guessing animal");

        Animal animal = Animal.builder().name("unknown").build();

        List<String> typicalProperties = properties.stream()
                .filter(AnimalProperty::isTypical)
                .map(AnimalProperty::getName)
                .collect(Collectors.toList());
        List<String> nonTypicalProperties = properties.stream()
                .filter(p -> !p.isTypical())
                .map(AnimalProperty::getName).collect(Collectors.toList());

        List<Animal> animals;
        if (isNotEmpty(typicalProperties) && isNotEmpty(nonTypicalProperties)) {
            animals = animalRepository.findAnimalsWithTheseProperties(typicalProperties,
                    nonTypicalProperties);
        } else if (isNotEmpty(typicalProperties)) {
            animals = animalRepository.findAnimalsWithTypicalProperties(typicalProperties);
        } else {
            animals = animalRepository.findAnimalsWithNonTypicalProperties(nonTypicalProperties);
        }

        if (isNotEmpty(animals) && animals.size() == 1) {
            animal = animals.get(0);
            log.info("Animal found:{}", animal.getName());
        }
        log.error("Finish guessing animal:{}", animal.getName());
        return animal;
    }

    /**
     * {@inheritDoc}
     */
    public void saveAnimal(RealAnimal realAnimal) {
        log.info("Start save or update animal");
        List<String> properties = realAnimal.getProperties().stream()
                .filter(AnimalProperty::isTypical)
                .map(AnimalProperty::getName)
                .collect(Collectors.toList());

        Animal animal = animalRepository.findAnimalByName(realAnimal.getName());
        if (nonNull(animal)) {
            log.info("Update existing animal:{}", animal.getName());
            final List<String> existedProperties = animal.getProperties();
            List<String> newProperties = properties.stream()
                    .filter(property -> !existedProperties.contains(property))
                    .collect(Collectors.toList());
            if (isNotEmpty(newProperties)) {
                List<String> finalProperties = new ArrayList<>(existedProperties);
                finalProperties.addAll(newProperties);
                animal.setProperties(finalProperties);
            }
        } else {
            animal = Animal.builder()
                    .name(realAnimal.getName())
                    .properties(properties)
                    .build();
        }
        animalRepository.save(animal);
        log.info("Finish save or update animal:{}, properties:{}", animal.getName(), animal.getProperties());
    }
}
