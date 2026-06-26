package blog.service;

import blog.model.Author;
import blog.repository.AuthorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthorDetailsService implements UserDetailsService {

    private final AuthorRepository authorRepository;

    public AuthorDetailsService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Author not found: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new User(author.getEmail(), author.getPassword(), Collections.singleton(authority));
    }
}

