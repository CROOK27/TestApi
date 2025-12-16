package Test;


import dto.PageDtoProductDto;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Products API")
@Feature("Управление продуктами")
@Owner("Максим")
public class ProductTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String PRODUCTS_ENDPOINT = BASE_URL + "/api/products";

    // ID категории "Электроника" (нужно получить из базы данных)
    private String electronicsCategoryId = "4"; // Пример ID, нужно заменить на реальный

    @BeforeAll
    @Step("Настройка базового URL")
    public static void setup() {
        baseURI = BASE_URL;
    }

    @Test
    @Story("Получение списка товаров с фильтрацией")
    @Description("ID: TC-025-BACK - Получение списка товаров с фильтрацией и пагинацией")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-025-BACK")
    public void testGetProductsWithFilterAndPagination() {
        // Предусловие: В базе данных создано 30 товаров в категории «Электроника»
        // Предполагаем, что есть товары "Геймпад" в ценовом диапазоне 2500-7000

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("categoryId", electronicsCategoryId);
        queryParams.put("name", "Геймпад");
        queryParams.put("minPrice", "2500");
        queryParams.put("maxPrice", "7000");
        queryParams.put("page", "0");
        queryParams.put("size", "20");

        Allure.addAttachment("Тестовые данные", "application/json",
                "GET " + PRODUCTS_ENDPOINT + "\n" +
                        "Параметры: " + queryParams.toString());

        Allure.step("Отправить GET-запрос с заданными тестовыми данными", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .queryParams(queryParams)
                    .when()
                    .get(PRODUCTS_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("pageNumber", notNullValue())
                    .body("pageSize", notNullValue())
                    .body("totalElementsCount", notNullValue())
                    .extract()
                    .response();

            String responseBody = response.getBody().asString();
            Allure.addAttachment("Ответ сервера", "application/json", responseBody);

            // Десериализуем ответ
            PageDtoProductDto pageDto = response.as(PageDtoProductDto.class);

            Allure.step("Проверка ожидаемого результата", () -> {
                assertNotNull(pageDto, "Ответ не должен быть null");
                assertEquals(0, pageDto.getPageNumber(), "Номер страницы должен быть 0");
                assertEquals(20, pageDto.getPageSize(), "Размер страницы должен быть 20");

                // Проверяем, что есть элементы
                assertNotNull(pageDto.getElements(), "Список элементов не должен быть null");

                // Проверяем общее количество элементов
                assertTrue(pageDto.getTotalElementsCount() > 0,
                        "Общее количество элементов должно быть больше 0");

                // Проверяем расчет общего количества страниц
                int expectedTotalPages = (int) Math.ceil(
                        (double) pageDto.getTotalElementsCount() / pageDto.getPageSize()
                );

                System.out.println("Результаты поиска:");
                System.out.println("- Номер страницы: " + pageDto.getPageNumber());
                System.out.println("- Размер страницы: " + pageDto.getPageSize());
                System.out.println("- Всего элементов: " + pageDto.getTotalElementsCount());
                System.out.println("- Элементов на странице: " +
                        (pageDto.getElements() != null ? pageDto.getElements().size() : 0));
                System.out.println("- Ожидаемое количество страниц: " + expectedTotalPages);

                // Если есть элементы, проверяем их свойства
                if (pageDto.getElements() != null && !pageDto.getElements().isEmpty()) {
                    for (PageDtoProductDto.ProductDtoElement product : pageDto.getElements()) {
                        assertNotNull(product.getId(), "ID продукта не должен быть null");
                        assertNotNull(product.getName(), "Название продукта не должно быть null");
                        assertNotNull(product.getPrice(), "Цена продукта не должна быть null");

                        // Проверяем фильтрацию по имени
                        if (queryParams.get("name") != null) {
                            assertTrue(product.getName().toLowerCase().contains("геймпад"),
                                    "Название продукта должно содержать 'Геймпад'");
                        }

                        // Проверяем фильтрацию по цене
                        if (queryParams.get("minPrice") != null && queryParams.get("maxPrice") != null) {
                            Long minPrice = Long.parseLong(queryParams.get("minPrice").toString());
                            Long maxPrice = Long.parseLong(queryParams.get("maxPrice").toString());
                            assertTrue(product.getPrice() >= minPrice && product.getPrice() <= maxPrice,
                                    "Цена продукта должна быть в диапазоне " + minPrice + "-" + maxPrice);
                        }
                    }
                }
            });

            System.out.println("TC-025-BACK: Список товаров с фильтрацией успешно получен.");
        });
    }

    @Test
    @Story("Получение списка товаров без фильтров")
    @Description("Получение всех товаров с пагинацией")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("PROD-026")
    public void testGetAllProducts() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", "0");
        queryParams.put("size", "10");

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParams(queryParams)
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        PageDtoProductDto pageDto = response.as(PageDtoProductDto.class);

        System.out.println("Все товары:");
        System.out.println("- Страница: " + pageDto.getPageNumber());
        System.out.println("- Размер: " + pageDto.getPageSize());
        System.out.println("- Всего: " + pageDto.getTotalElementsCount());
        System.out.println("- На странице: " +
                (pageDto.getElements() != null ? pageDto.getElements().size() : 0));
    }

    @Test
    @Story("Создание товара")
    @Description("Создание нового товара")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("PROD-027")
    public void testCreateProduct() {
        String authToken = getAuthToken();

        String requestBody = String.format(
                "{\"name\": \"Тестовый товар %s\", " +
                        "\"characteristics\": \"Характеристики тестового товара\", " +
                        "\"price\": 5000, " +
                        "\"count\": 10, " +
                        "\"categoryId\": \"%s\"}",
                System.currentTimeMillis(),
                electronicsCategoryId
        );

        Allure.addAttachment("Тестовые данные", "application/json", requestBody);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .body(requestBody)
                .when()
                .post(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(200)
                .log().all();

        System.out.println("Товар успешно создан");
    }

    @Test
    @Story("Получение товара по ID")
    @Description("Получение информации о конкретном товаре")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("PROD-028")
    public void testGetProductById() {
        // Нужен ID существующего товара
        String productId = "123e4567-e89b-12d3-a456-426614174001";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", productId)
                .when()
                .get(PRODUCTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(anyOf(is(200), is(404)))
                .log().all();

        System.out.println("Запрос товара по ID: " + productId);
    }

    // Вспомогательный метод для получения токена
    private String getAuthToken() {
        // Реализация получения токена
        return "ваш_токен_авторизации";
    }
}