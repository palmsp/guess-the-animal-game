package com.palms.animal.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("DTO for animal property info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalProperty {

    @ApiModelProperty("Property name")
    private String name;

    @ApiModelProperty("Shows is it typical for animal or not")
    private boolean typical;
}
