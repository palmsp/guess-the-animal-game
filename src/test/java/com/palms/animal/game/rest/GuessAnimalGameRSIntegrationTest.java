package com.palms.animal.game.rest;

import static com.palms.animal.game.rest.ApiEndpoints.ANIMAL_GAME;
import static com.palms.animal.game.rest.ApiEndpoints.DIFFERENCE;
import static com.palms.animal.game.rest.ApiEndpoints.GUESSED_ANIMAL;
import static com.palms.animal.game.rest.ApiEndpoints.PROPERTIES;
import static com.palms.animal.game.rest.ApiEndpoints.REAL_ANIMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.domain.Property;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.RealAnimal;
import com.palms.animal.game.repository.AnimalRepository;
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
    protected AnimalRepository animalRepository;
    @Autowired
    protected PropertyRepository propertyRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        List<String> properties = List.of("like honey", "hibernate during winter");
        Animal animal = Animal.builder().name("bear").properties(properties).build();

        animalRepository.save(animal);
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

        assertThat(properties).hasSize(1);
    }

    @Test
    void shouldFetchGuessedAnimal() throws Exception {
        AnimalProperty animalProperty = AnimalProperty.builder().name("like honey").typical(true).build();
        List<AnimalProperty> properties = List.of(animalProperty);

        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + GUESSED_ANIMAL)
                .content(objectMapper.writeValueAsString(properties))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        String response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Animal animal = objectMapper.readValue(response, Animal.class);

        assertThat(animal).isNotNull();
        assertThat(animal.getName()).isEqualTo("bear");
    }

    @Test
    void shouldSaveRealAnimal() throws Exception {
        AnimalProperty animalProperty = AnimalProperty.builder().name("has wings").build();
        RealAnimal realAnimal = RealAnimal.builder().name("goose").properties(List.of(animalProperty)).build();

        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + REAL_ANIMAL)
                .content(objectMapper.writeValueAsString(realAnimal))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Animal animal = animalRepository.findAnimalByName(realAnimal.getName());
        assertThat(animal).isNotNull();
        assertThat(animal.getName()).isEqualTo(realAnimal.getName());
    }

    @Test
    void shouldSaveDifferenceProperty() throws Exception {
        MockHttpServletRequestBuilder request = post(ANIMAL_GAME + DIFFERENCE)
                .content("live in forest")
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

        assertThat(properties).hasSize(2);
        assertThat(properties).contains("live in forest");
    }
}