package dto;


import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class ListCategoryDto {
    private List<CategoryItem> categories;

    @Data
    public static class CategoryItem {
        private UUID id;
        private String name;
    }
}