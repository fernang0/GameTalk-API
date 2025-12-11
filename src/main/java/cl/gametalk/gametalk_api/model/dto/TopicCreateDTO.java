package cl.gametalk.gametalk_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicCreateDTO {
    private Integer categoryId;
    private Integer userId;
    private String title;
    private String description;
}
