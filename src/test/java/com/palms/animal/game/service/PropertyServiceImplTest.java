package com.palms.animal.game.service;

import static com.palms.animal.game.AnimalPropertyTestData.CAN_FLY;
import static com.palms.animal.game.AnimalPropertyTestData.CAN_SWIM;
import static com.palms.animal.game.AnimalPropertyTestData.HAS_FUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.palms.animal.game.domain.Property;
import com.palms.animal.game.repository.PropertyRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void shouldSaveProperty() {
        propertyService.saveProperty(CAN_SWIM);

        verify(propertyRepository).save(any(Property.class));
    }
}