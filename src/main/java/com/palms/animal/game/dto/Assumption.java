package com.palms.animal.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("DTO for assumption")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assumption {

    @ApiModelProperty("Value can be animal or question")
    private String value;

    @ApiModelProperty("True if it's animal")
    private Boolean isAnimal;
}
