package Test.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class PageDtoProductDto {
    private String description;
    private List<ProductDtoElement> elements;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElementsCount;

    @Data
    public static class ProductDtoElement {
        private UUID id;
        private String name;
        private String characteristics;
        private Long price;
        private Long count;
        private Double rating;
    }
}