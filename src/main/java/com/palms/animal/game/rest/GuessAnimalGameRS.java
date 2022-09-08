package com.palms.animal.game.rest;

import static com.palms.animal.game.rest.ApiEndpoints.ANIMAL_GAME;
import static com.palms.animal.game.rest.ApiEndpoints.DIFFERENCE;
import static com.palms.animal.game.rest.ApiEndpoints.GUESSED_ANIMAL;
import static com.palms.animal.game.rest.ApiEndpoints.PROPERTIES;
import static com.palms.animal.game.rest.ApiEndpoints.REAL_ANIMAL;

import com.palms.animal.game.domain.Animal;
import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.RealAnimal;
import com.palms.animal.game.service.AnimalService;
import com.palms.animal.game.service.PropertyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to provide  animal game logic.
 */
@RestController
@RequestMapping(ANIMAL_GAME)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class GuessAnimalGameRS {

    @Autowired
    private AnimalService animalService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping(PROPERTIES)
    @ApiOperation("Get all properties")
    public List<String> getAllProperties() {
        return propertyService.getProperties();
    }

    @PostMapping(GUESSED_ANIMAL)
    @ApiOperation("Returns guessed animal")
    public Animal fetchGuessedAnimal(
            @ApiParam("Properties to guess animal")
            @RequestBody List<AnimalProperty> properties) {
        return animalService.fetchGuessedAnimal(properties);
    }

    @PostMapping(REAL_ANIMAL)
    @ApiOperation("Save real animal")
    public void saveRealAnimal(
            @ApiParam("Real animal info")
            @RequestBody RealAnimal realAnimal) {
        animalService.saveAnimal(realAnimal);
    }

    @PostMapping(DIFFERENCE)
    @ApiOperation("Save difference between animals")
    public void saveDifferenceProperty(
            @ApiParam("Difference")
            @RequestBody String difference) {
        propertyService.saveProperty(difference);
    }

}
