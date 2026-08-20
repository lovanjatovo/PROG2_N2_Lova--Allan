package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String ref;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}