package cl.gametalk.gametalk_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topics", indexes = {
    @Index(name = "idx_topics_categoryId", columnList = "categoryId"),
    @Index(name = "idx_topics_userId", columnList = "userId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private Integer categoryId;
    
    @Column(nullable = false)
    private Integer userId;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Long createdAt;
    
    @Column(nullable = false)
    private Integer repliesCount = 0;
    
    @Column(nullable = false)
    private Integer viewsCount = 0;
    
    @Column(nullable = false)
    private Long lastActivity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
}
