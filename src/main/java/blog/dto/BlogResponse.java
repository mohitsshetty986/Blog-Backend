package blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String category;
    private String excerpt;
    private String authorName;
    private Long authorId;
    private String readTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
