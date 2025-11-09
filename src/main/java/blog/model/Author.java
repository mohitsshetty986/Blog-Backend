package blog.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // optional extra fields
    private String email;
    private String password;
}
