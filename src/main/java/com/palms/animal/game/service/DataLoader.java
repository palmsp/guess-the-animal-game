package com.palms.animal.game.service;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.repository.AnimalRepository;
import com.palms.animal.game.repository.PropertyRepository;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Class for loading initial game data.
 */
@Component
@AllArgsConstructor
public class DataLoader {

    private AnimalRepository animalRepository;
    private PropertyRepository propertyRepository;

    @PostConstruct
    private void loadData() {
        final String property = "живет на суше";
        Animal animal1 = Animal.builder().name("кит").properties(Collections.emptyList()).build();
        Animal animal2 = Animal.builder().name("кот").properties(List.of(property)).build();

        animalRepository.saveAll(List.of(animal1, animal2));
        propertyRepository.save(Property.builder().name(property).build());
    }

}
