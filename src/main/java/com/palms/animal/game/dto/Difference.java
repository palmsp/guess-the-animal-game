package com.palms.animal.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("DTO for difference between animals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Difference {

    @ApiModelProperty("Animal which user guessed")
    private String correctAnimal;

    @ApiModelProperty("Previous property")
    private String oldProperty;

    @ApiModelProperty("New property is difference")
    private String newProperty;

    @ApiModelProperty("Is old property typical")
    private Boolean isOldTypical;
}
