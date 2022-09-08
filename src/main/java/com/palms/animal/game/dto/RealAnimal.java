package com.palms.animal.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("DTO for real animal info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealAnimal {

    @ApiModelProperty("Property name")
    private String name;

    @ApiModelProperty("List of properties")
    private List<AnimalProperty> properties;
}
