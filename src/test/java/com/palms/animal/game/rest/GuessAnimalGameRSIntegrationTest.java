package com.palms.animal.game.rest;

import static com.palms.animal.game.AnimalPropertyTestData.HAS_LONG_TALE;
import static com.palms.animal.game.AnimalPropertyTestData.LIVE_IN_FOREST;
import static com.palms.animal.game.rest.ApiEndpoints.ANIMAL_GAME;
import static com.palms.animal.game.rest.ApiEndpoints.DIFFERENCE;
import static com.palms.animal.game.rest.ApiEndpoints.ASSUMPTION;
import static com.palms.animal.game.rest.ApiEndpoints.PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palms.animal.game.domain.Node;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.Difference;
import com.palms.animal.game.dto.Assumption;
import com.palms.animal.game.repository.PropertyRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class GuessAnimalGameRSIntegrationTest {

    @Autowired
    protected PropertyRepository propertyRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        Property property1 = Property.builder()
                .name("live in forest")
                .left(Node.builder().value("bear").isAnimal(true).build())
                .right(Node.builder().value("camel").isAnimal(true).build())
                .build();

        Property property2 = Property.builder()
                .name("can swim")
                .left(Node.builder().value("live in ocean").isAnimal(false).build())
                .right(Node.builder().value("live in lake").isAnimal(false).build())
                .build();
        propertyRepository.saveAll(List.of(property1, property2));
    }

    @Test
    void shouldReturnAllProperties() throws Exception {
        MockHttpServletRequestBuilder request = get(ANIMAL_GAME + PROPERTIES)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        String response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<String> properties = objectMapper.readValue(response, new TypeReference<List<String>>() {
        });

        assertThat(properties).hasSize(3);
    }

    @Test
    public void shouldReturnAssumptionWithAnimal() throws Exception {
        AnimalProperty answer = AnimalProperty.builder()
                .name("live in forest")
                .isTypical(true)
                .build();
        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + ASSUMPTION)
                .content(objectMapper.writeValueAsString(answer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        String response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assumption assumption = objectMapper.readValue(response, Assumption.class);

        assertThat(assumption).isNotNull();
        assertThat(assumption.getValue()).isEqualTo("bear");
        assertThat(assumption.getIsAnimal()).isEqualTo(true);
    }

    @Test
    public void shouldReturnAssumptionWithQuestion() throws Exception {
        AnimalProperty answer = AnimalProperty.builder()
                .name("can swim")
                .isTypical(true)
                .build();
        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + ASSUMPTION)
                .content(objectMapper.writeValueAsString(answer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        String response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assumption assumption = objectMapper.readValue(response, Assumption.class);

        assertThat(assumption).isNotNull();
        assertThat(assumption.getValue()).isEqualTo("live in ocean");
        assertThat(assumption.getIsAnimal()).isEqualTo(false);
    }

    @Test
    void shouldSaveDifferenceProperty() throws Exception {
        Difference difference = Difference.builder()
                .correctAnimal("fox")
                .newProperty(HAS_LONG_TALE)
                .oldProperty(LIVE_IN_FOREST)
                .isOldTypical(false)
                .build();

        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + DIFFERENCE)
                .content(objectMapper.writeValueAsString(difference))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> properties = propertyRepository.findAll().stream()
                .map(Property::getName)
                .collect(Collectors.toList());

        assertThat(properties).hasSize(4);
        assertThat(properties).contains(HAS_LONG_TALE);
    }
}