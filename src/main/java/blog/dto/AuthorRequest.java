package blog.dto;

import lombok.Data;

@Data
public class AuthorRequest {
    private String name;
    private String email;
    private String password;
}