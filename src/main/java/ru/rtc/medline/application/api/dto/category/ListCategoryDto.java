package ru.rtc.medline.application.api.dto.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCategoryDto {

    List<CategoryDto> categories;

}
