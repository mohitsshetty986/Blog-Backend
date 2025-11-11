package blog.controller;

import blog.dto.AuthorRequest;
import blog.dto.LoginRequest;
import blog.model.Author;
import blog.repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Sign up
    @PostMapping("/signup")
    public ResponseEntity<Author> signup(@RequestBody AuthorRequest request) {
        // Check if email already exists
        if (authorRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Author author = new Author();
        author.setName(request.getName());
        author.setEmail(request.getEmail());
        author.setPassword(request.getPassword()); // In production, hash password
        Author saved = authorRepository.save(author);

        return ResponseEntity.ok(saved);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return authorRepository.findByEmail(request.getEmail())
                .filter(a -> a.getPassword().equals(request.getPassword()))
                .map(a -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("author", a); // optional
                    response.put("token", "dummy-token"); // if you want JWT
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("success", false)));
    }

}
