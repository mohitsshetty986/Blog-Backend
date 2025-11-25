package blog.controller;

import blog.dto.BlogRequest;
import blog.dto.BlogResponse;
import blog.model.Blog;
import blog.repository.AuthorRepository;
import blog.repository.BlogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://blog-frontend-rho-one.vercel.app")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogRepository blogRepository;
    private final AuthorRepository authorRepository;

    public BlogController(BlogRepository blogRepository, AuthorRepository authorRepository) {
        this.blogRepository = blogRepository;
        this.authorRepository = authorRepository;
    }

    // Convert Blog -> BlogResponse
    private BlogResponse toResponse(Blog blog) {
        BlogResponse res = new BlogResponse();
        res.setId(blog.getId());
        res.setTitle(blog.getTitle());
        res.setContent(blog.getContent());
        res.setImageUrl(blog.getImageUrl());
        res.setCategory(blog.getCategory());
        res.setExcerpt(blog.getExcerpt());
        res.setAuthorId(blog.getAuthor().getId());
        res.setAuthorName(blog.getAuthor().getName());
        res.setCreatedTime(blog.getCreatedTime());
        res.setUpdatedTime(blog.getUpdatedTime());
        res.setReadTime(blog.getReadTime());
        return res;
    }

    // GET all blogs
    @GetMapping
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // GET blog by id
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> ResponseEntity.ok(toResponse(blog)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create new blog
    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request) {
        return authorRepository.findById(request.getAuthorId())
                .map(author -> {
                    Blog blog = new Blog();
                    blog.setTitle(request.getTitle());
                    blog.setContent(request.getContent());
                    blog.setCategory(request.getCategory());
                    blog.setExcerpt(request.getExcerpt());
                    blog.setImageUrl(request.getImageUrl());
                    blog.setAuthor(author);
                    blog.setReadTime(request.getReadTime()); // default read time
                    Blog saved = blogRepository.save(blog);
                    return ResponseEntity.ok(toResponse(saved));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    // PATCH update blog partially (only content/title/author)
    @PatchMapping("/{id}")
    public ResponseEntity<BlogResponse> patchBlog(@PathVariable Long id, @RequestBody BlogRequest request) {
        return blogRepository.findById(id)
                .map(existing -> {
                    if (request.getTitle() != null) existing.setTitle(request.getTitle());
                    if (request.getContent() != null) existing.setContent(request.getContent());
                    if (request.getImageUrl() != null) existing.setImageUrl(request.getImageUrl());
                    if (request.getAuthorId() != null) {
                        authorRepository.findById(request.getAuthorId())
                                .ifPresent(existing::setAuthor);
                    }
                    if (request.getCategory() != null) existing.setCategory(request.getCategory());
                    if (request.getExcerpt() != null) existing.setExcerpt(request.getExcerpt());
                    if (request.getReadTime() != null) existing.setReadTime(request.getReadTime());
                    Blog updated = blogRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE blog
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blogRepository.delete(blog);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
