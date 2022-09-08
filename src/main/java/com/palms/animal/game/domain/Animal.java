package com.palms.animal.game.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "animals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Animal {

    @Id
    private String id;

    private String name;
    private List<String> properties;
}
