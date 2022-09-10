package com.palms.animal.game.service;

import static com.palms.animal.game.AnimalPropertyTestData.CAN_FLY;
import static com.palms.animal.game.AnimalPropertyTestData.CAN_SWIM;
import static com.palms.animal.game.AnimalPropertyTestData.HAS_FUR;
import static com.palms.animal.game.AnimalPropertyTestData.HAS_LONG_TALE;
import static com.palms.animal.game.AnimalPropertyTestData.LIVE_IN_FOREST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.palms.animal.game.domain.Node;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.dto.Difference;
import com.palms.animal.game.dto.Assumption;
import com.palms.animal.game.repository.PropertyRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @InjectMocks
    private PropertyServiceImpl propertyService;
    @Mock
    private PropertyRepository propertyRepository;

    @Test
    public void shouldReturnProperties() {
        Property property1 = Property.builder().name(CAN_FLY).build();
        Property property2 = Property.builder().name(CAN_SWIM).build();
        Property property3 = Property.builder().name(HAS_FUR).build();

        List<Property> expectedProperties = List.of(property1, property2, property3);
        when(propertyRepository.findAll()).thenReturn(expectedProperties);

        List<String> properties = propertyService.getProperties();

        assertThat(properties).hasSize(3);
        assertThat(properties).containsExactlyInAnyOrder(CAN_SWIM, CAN_FLY, HAS_FUR);
    }

    @Test
    public void shouldReturnEmptyProperties() {
        List<String> properties = propertyService.getProperties();
        assertThat(properties).hasSize(0);
    }

    @Test
    void shouldReturnAssumptionWithAnimal() {
        String askedQuestion = "live in forest";
        Property property = Property.builder()
                .name(askedQuestion)
                .left(Node.builder().value("bear").isAnimal(true).build())
                .build();
        when(propertyRepository.findPropertyByName(askedQuestion)).thenReturn(property);

        Assumption assumption = propertyService.getAssumption(askedQuestion, true);

        assertThat(assumption).isNotNull();
        assertThat(assumption.getValue()).isEqualTo(property.getLeft().getValue());
        assertThat(assumption.getIsAnimal()).isEqualTo(property.getLeft().getIsAnimal());
    }

    @Test
    void shouldReturnAssumptionWithQuestionWhenYes() {
        String askedQuestion = "live in forest";
        Property property = Property.builder()
                .name(askedQuestion)
                .left(Node.builder().value("like honey").isAnimal(false).build())
                .right(Node.builder().value("predator").isAnimal(false).build())
                .build();
        when(propertyRepository.findPropertyByName(askedQuestion)).thenReturn(property);

        Assumption assumption = propertyService.getAssumption(askedQuestion, true);

        assertThat(assumption).isNotNull();
        assertThat(assumption.getValue()).isEqualTo(property.getLeft().getValue());
        assertThat(assumption.getIsAnimal()).isEqualTo(false);
    }

    @Test
    void shouldReturnAssumptionWithQuestionWhenNo() {
        String askedQuestion = "live in forest";
        Property property = Property.builder()
                .name(askedQuestion)
                .left(Node.builder().value("like honey").isAnimal(false).build())
                .right(Node.builder().value("predator").isAnimal(false).build())
                .build();
        when(propertyRepository.findPropertyByName(askedQuestion)).thenReturn(property);

        Assumption assumption = propertyService.getAssumption(askedQuestion, false);

        assertThat(assumption).isNotNull();
        assertThat(assumption.getValue()).isEqualTo(property.getRight().getValue());
        assertThat(assumption.getIsAnimal()).isEqualTo(false);
    }

    @Test
    void shouldSavePropertiesWhenTypical() {
        Property property = Property.builder()
                .name(LIVE_IN_FOREST)
                .left(Node.builder().value("bear").isAnimal(true).build())
                .right(Node.builder().value("whale").isAnimal(true).build())
                .build();
        when(propertyRepository.findPropertyByName(LIVE_IN_FOREST)).thenReturn(property);

        Difference difference = Difference.builder()
                .correctAnimal("fox")
                .newProperty(HAS_LONG_TALE)
                .oldProperty(LIVE_IN_FOREST)
                .isOldTypical(true)
                .build();

        propertyService.saveProperty(difference);

        ArgumentCaptor<Property> argument = ArgumentCaptor.forClass(Property.class);
        verify(propertyRepository, times(2)).save(argument.capture());

        List<Property> properties = argument.getAllValues();

        Property property1 = properties.stream().filter(p -> p.getName().equals(LIVE_IN_FOREST)).findFirst()
                .orElse(null);
        assertThat(property1).isNotNull();
        assertThat(property1.getLeft().getIsAnimal()).isFalse();
        assertThat(property1.getLeft().getValue().equals(LIVE_IN_FOREST));
        assertThat(property1.getRight().getIsAnimal()).isTrue();
        assertThat(property1.getRight().getValue().equals("whale"));

        Property property2 = properties.stream().filter(p -> p.getName().equals(HAS_LONG_TALE)).findFirst()
                .orElse(null);
        assertThat(property2).isNotNull();
        assertThat(property2.getLeft().getIsAnimal()).isTrue();
        assertThat(property1.getLeft().getValue().equals("fox"));
        assertThat(property1.getRight().getIsAnimal()).isTrue();
        assertThat(property1.getRight().getValue().equals("bear"));
    }

    @Test
    void shouldSavePropertiesWhenNotTypical() {
        Property property = Property.builder()
                .name(LIVE_IN_FOREST)
                .left(Node.builder().value("bear").isAnimal(true).build())
                .right(Node.builder().value("whale").isAnimal(true).build())
                .build();
        when(propertyRepository.findPropertyByName(LIVE_IN_FOREST)).thenReturn(property);

        Difference difference = Difference.builder()
                .correctAnimal("fox")
                .newProperty(HAS_LONG_TALE)
                .oldProperty(LIVE_IN_FOREST)
                .isOldTypical(false)
                .build();

        propertyService.saveProperty(difference);

        ArgumentCaptor<Property> argument = ArgumentCaptor.forClass(Property.class);
        verify(propertyRepository, times(2)).save(argument.capture());

        List<Property> properties = argument.getAllValues();

        Property property1 = properties.stream().filter(p -> p.getName().equals(LIVE_IN_FOREST)).findFirst()
                .orElse(null);
        assertThat(property1).isNotNull();

        assertThat(property1.getLeft().getIsAnimal()).isTrue();
        assertThat(property1.getLeft().getValue().equals("whale"));
        assertThat(property1.getRight().getIsAnimal()).isFalse();
        assertThat(property1.getRight().getValue().equals(LIVE_IN_FOREST));

        Property property2 = properties.stream().filter(p -> p.getName().equals(HAS_LONG_TALE)).findFirst()
                .orElse(null);
        assertThat(property2).isNotNull();

        assertThat(property1.getLeft().getIsAnimal()).isTrue();
        assertThat(property1.getLeft().getValue().equals("bear"));
        assertThat(property2.getRight().getIsAnimal()).isTrue();
        assertThat(property1.getRight().getValue().equals("fox"));
    }
}