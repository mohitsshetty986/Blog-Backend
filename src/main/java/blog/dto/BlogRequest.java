package blog.dto;

import lombok.Data;

@Data
public class BlogRequest {
    private String title; // title of the blog
    private String imageUrl; // image URL
    private String category; // category of the blog
    private String content; // markdown text
    private Long authorId; // reference to Author
    private String excerpt; // short excerpt of the blog
    private String readTime; // time to read the blog
}
