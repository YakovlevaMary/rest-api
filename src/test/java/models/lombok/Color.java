package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Color {

    private Integer ID;
    private String name;
    private String year;
    private String color;
    private String pantone_value;
}