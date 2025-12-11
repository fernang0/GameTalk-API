package cl.gametalk.gametalk_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Integer id;
    private Integer categoryId;
    private Integer userId;
    private String title;
    private String description;
    private Long createdAt;
    private Integer repliesCount;
    private Integer viewsCount;
    private Long lastActivity;
    private String categoryName;
    private String username;
}
