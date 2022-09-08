package com.palms.animal.game.repository;

import static com.palms.animal.game.AnimalPropertyTestData.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.repository.AnimalRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class AnimalRepositoryIntegrationTest {

    @Autowired
    private AnimalRepository repository;

    @BeforeEach
    public void setUp() {

        Animal animal1 = new Animal();
        animal1.setName("Goose");
        animal1.setProperties(List.of(CAN_FLY, CAN_SWIM, HAS_WINGS));

        Animal animal2 = new Animal();
        animal2.setName("Cat");
        animal2.setProperties(List.of(CAN_SWIM, HAS_FUR));

        Animal animal3 = new Animal();
        animal3.setName("Dog");
        animal3.setProperties(List.of(CAN_SWIM, EAT_BONES, HAS_FUR));

        repository.saveAll(List.of(animal1, animal2, animal3));
    }

    @AfterEach
    public void cleanRepository() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnAllAnimals() {
        List<Animal> animals = repository.findAll();

        assertThat(animals).hasSize(3);
    }


    @Test
    public void shouldReturnAnimalByName() {
        Animal animal = repository.findAnimalByName("Dog");

        assertThat(animal).isNotNull();
        assertThat(animal.getProperties()).containsExactlyInAnyOrder(EAT_BONES, HAS_FUR, CAN_SWIM);
    }

    @Test
    public void shouldReturnAllWithOneProperty() {
        List<Animal> animals = repository.findAnimalsByPropertiesContaining(CAN_FLY);

        assertThat(animals).hasSize(1);
        assertThat(animals).map(Animal::getName).contains("Goose");
    }

    @Test
    public void shouldReturnAllWithFewProperties() {
        List<Animal> animals = repository.findAnimalsWithTheseProperties(List.of(CAN_SWIM, HAS_FUR), emptyList());

        assertThat(animals).hasSize(2);
        assertThat(animals).map(Animal::getName).containsExactlyInAnyOrder("Dog", "Cat");
    }

    @Test
    public void shouldUpdateAnimalWithProperties() {
        final String likeBall = "like ball";

        Animal animal = repository.findAnimalByName("Dog");
        animal.getProperties().add(likeBall);

        repository.save(animal);

        Animal updatedAnimal = repository.findAnimalByName("Dog");
        List<Animal> animals = repository.findAll();

        assertThat(updatedAnimal).isNotNull();
        assertThat(updatedAnimal.getProperties()).hasSize(4);
        assertThat(updatedAnimal.getProperties()).containsExactlyInAnyOrder(HAS_FUR, CAN_SWIM, EAT_BONES, likeBall);

        assertThat(animals).hasSize(3);
    }
}
