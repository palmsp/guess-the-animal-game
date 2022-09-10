package com.palms.animal.game.service;

import com.palms.animal.game.domain.Node;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.repository.PropertyRepository;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Class for loading initial game data.
 */
@Component
@AllArgsConstructor
public class DataLoader {

    private PropertyRepository propertyRepository;

    @PostConstruct
    private void loadData() {
        final String property = "живет на суше";
        Node node1 = Node.builder().value("кот").isAnimal(true).build();
        Node node2 = Node.builder().value("кит").isAnimal(true).build();

        propertyRepository.save(Property.builder()
                .name(property)
                .left(node1)
                .right(node2)
                .build());
    }

}
