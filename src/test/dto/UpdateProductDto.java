package dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateProductDto {
    private UUID id;
    private String name;
    private String characteristics;
    private Long price;
    private Long count;
    private UUID categoryId;
}