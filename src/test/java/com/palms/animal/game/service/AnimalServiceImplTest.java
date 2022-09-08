package com.palms.animal.game.service;

import static com.palms.animal.game.AnimalPropertyTestData.CAN_FLY;
import static com.palms.animal.game.AnimalPropertyTestData.EAT_BONES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.RealAnimal;
import com.palms.animal.game.repository.AnimalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {

    @InjectMocks
    private AnimalServiceImpl animalService;
    @Mock
    private AnimalRepository animalRepository;

    @Test
    public void shouldReturnGuessedAnimal() {
        AnimalProperty animalProperty1 = AnimalProperty.builder().name(CAN_FLY).typical(true).build();
        AnimalProperty animalProperty2 = AnimalProperty.builder().name(EAT_BONES).typical(false).build();
        List<AnimalProperty> animalProperties = List.of(animalProperty1, animalProperty2);

        List<Animal> animals = List.of(Animal.builder().name("Goose").build());
        when(animalRepository.findAnimalsWithTheseProperties(any(List.class), any(List.class))).thenReturn(animals);

        Animal animal = animalService.fetchGuessedAnimal(animalProperties);

        assertThat(animal).isNotNull();
        assertThat(animal.getName()).isEqualTo(animals.get(0).getName());
    }

    @Test
    public void shouldReturnUnknownAnimal() {
        AnimalProperty animalProperty1 = AnimalProperty.builder().name(CAN_FLY).typical(true).build();
        AnimalProperty animalProperty2 = AnimalProperty.builder().name(EAT_BONES).typical(false).build();
        List<AnimalProperty> animalProperties = List.of(animalProperty1, animalProperty2);

        when(animalRepository.findAnimalsWithTheseProperties(any(List.class), any(List.class))).thenReturn(null);

        Animal animal = animalService.fetchGuessedAnimal(animalProperties);

        assertThat(animal).isNotNull();
        assertThat(animal.getName()).isEqualTo("unknown");
    }

    @Test
    void shouldSaveAnimal() {
        AnimalProperty animalProperty = AnimalProperty.builder().name("like honey").build();
        RealAnimal realAnimal = RealAnimal.builder().name("bear").properties(List.of(animalProperty)).build();

        animalService.saveAnimal(realAnimal);

        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void shouldUpdateAnimal() {
        Animal existedAnimal = Animal.builder().name("bear").properties(List.of("live in forest")).build();
        when(animalRepository.findAnimalByName("bear")).thenReturn(existedAnimal);

        AnimalProperty animalProperty1 = AnimalProperty.builder().name("like honey").typical(true).build();
        AnimalProperty animalProperty2 = AnimalProperty.builder().name("hibernate during winter").typical(true).build();

        RealAnimal realAnimal = RealAnimal.builder().name("bear").properties(List.of(animalProperty1, animalProperty2))
                .build();

        List<String> expectedProperties = new ArrayList<>();
        expectedProperties.addAll(existedAnimal.getProperties());
        expectedProperties.addAll(
                realAnimal.getProperties().stream().map(AnimalProperty::getName).collect(Collectors.toList()));

        animalService.saveAnimal(realAnimal);

        verify(animalRepository).save(
                Animal.builder().name(existedAnimal.getName()).properties(expectedProperties).build());
    }

}