package com.palms.animal.game.rest;

import static com.palms.animal.game.rest.ApiEndpoints.ANIMAL_GAME;
import static com.palms.animal.game.rest.ApiEndpoints.DIFFERENCE;
import static com.palms.animal.game.rest.ApiEndpoints.ASSUMPTION;
import static com.palms.animal.game.rest.ApiEndpoints.PROPERTIES;

import com.palms.animal.game.dto.AnimalProperty;
import com.palms.animal.game.dto.Difference;
import com.palms.animal.game.dto.Assumption;
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
    private PropertyService propertyService;

    @GetMapping(PROPERTIES)
    @ApiOperation("Get all properties")
    public List<String> getAllProperties() {
        return propertyService.getProperties();
    }

    @PostMapping(DIFFERENCE)
    @ApiOperation("Save difference between animals")
    public void saveDifferenceProperty(
            @ApiParam("Difference")
            @RequestBody Difference difference) {
        propertyService.saveProperty(difference);
    }

    @PostMapping(ASSUMPTION)
    @ApiOperation("Return assumption. It can be next question or guessed animal")
    public Assumption getAssumption(
            @ApiParam("Property")
            @RequestBody AnimalProperty property) {
        return propertyService.getAssumption(property.getName(), property.getIsTypical());
    }
}
