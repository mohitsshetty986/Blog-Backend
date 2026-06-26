package blog.controller;

import blog.dto.AuthorRequest;
import blog.dto.LoginRequest;
import blog.model.Author;
import blog.repository.AuthorRepository;
import blog.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "https://blog-frontend-rho-one.vercel.app")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthorController(AuthorRepository authorRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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
        author.setPassword(passwordEncoder.encode(request.getPassword()));
        Author saved = authorRepository.save(author);

        return ResponseEntity.ok(saved);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return authorRepository.findByEmail(request.getEmail())
                .filter(a -> passwordEncoder.matches(request.getPassword(), a.getPassword()))
                .map(a -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("author", a); // optional
                    String token = jwtUtils.generateToken(a.getEmail());
                    response.put("token", token);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("success", false)));
    }

}
