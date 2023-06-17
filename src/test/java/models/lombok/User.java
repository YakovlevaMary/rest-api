package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer ID;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}