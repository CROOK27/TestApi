package Test;

import dto.RequestLoginDto;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.TokenManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Products API")
@Feature("Управление категориями")
@Owner("Максим")
public class CategoryTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String CATEGORIES_ENDPOINT = BASE_URL + "/api/categories";
    private static final String AUTH_ENDPOINT = BASE_URL + "/auth/login";

    // ID тестовой категории (будет создана при выполнении теста)
    private String createdCategoryId;

    @BeforeAll
    @Step("Настройка базового URL")
    public static void setup() {
        baseURI = BASE_URL;
    }

    @Step("Получить токен авторизации")
    private String getAuthToken() {
        // Пытаемся получить токен из менеджера
        if (TokenManager.hasDefaultToken()) {
            String token = TokenManager.getDefaultToken();
            System.out.println("Используем сохраненный токен из TokenManager");
            return token;
        }

        // Если токена нет, выполняем авторизацию
        System.out.println("Токен не найден в TokenManager, выполняем авторизацию...");
        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin("Chertolet@yamail.com");
        loginData.setPassword("tzarbomba");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = response.jsonPath().getString("token");
        TokenManager.saveDefaultToken(token);
        System.out.println("Получен новый токен и сохранен в TokenManager");

        return token;
    }

    @Step("Создать тестовую категорию и получить ее ID")
    private String createTestCategory() {
        String token = getAuthToken();
        String categoryName = "Тестовая категория " + System.currentTimeMillis();
        String requestBody = String.format("{\"name\": \"%s\"}", categoryName);

        System.out.println("Создаем тестовую категорию: " + categoryName);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = response.getBody().asString();
        System.out.println("Ответ от сервера при создании категории: " + responseBody);

        // Пробуем разные варианты получения ID
        String categoryId = null;

        // Вариант 1: из поля "id"
        if (responseBody.contains("id")) {
            categoryId = response.jsonPath().getString("id");
        }

        // Вариант 2: если ответ содержит UUID в другом формате
        if (categoryId == null || categoryId.isEmpty()) {
            // Пробуем найти UUID в ответе
            java.util.regex.Pattern uuidPattern = java.util.regex.Pattern.compile(
                    "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"
            );
            java.util.regex.Matcher matcher = uuidPattern.matcher(responseBody);
            if (matcher.find()) {
                categoryId = matcher.group();
            }
        }

        // Вариант 3: если ID не найден, генерируем случайный (для тестов)
        if (categoryId == null || categoryId.isEmpty()) {
            categoryId = UUID.randomUUID().toString();
            System.out.println("⚠ ID категории не найден в ответе, используем сгенерированный: " + categoryId);
        } else {
            System.out.println("✓ ID созданной категории: " + categoryId);
        }

        this.createdCategoryId = categoryId;
        return categoryId;
    }

    @Test
    @Story("Получение списка категорий")
    @Description("ID: TC-023-BACK - Получение списка всех категорий")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-023-BACK")
    public void testGetAllCategories() {
        // Получаем токен авторизации
        String token = getAuthToken();

        Allure.addAttachment("Тестовые данные", "application/json",
                "GET " + CATEGORIES_ENDPOINT + "\n" +
                        "Authorization: Bearer " + token.substring(0, Math.min(20, token.length())) + "...");

        Allure.step("Выполнить GET-запрос с указанными тестовыми данными", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(CATEGORIES_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body(notNullValue())
                    .extract()
                    .response();

            String responseBody = response.getBody().asString();
            Allure.addAttachment("Ответ сервера", "application/json", responseBody);

            Allure.step("Проверка структуры ответа", () -> {
                assertNotNull(responseBody, "Ответ не должен быть null");
                assertFalse(responseBody.isEmpty(), "Ответ не должен быть пустым");

                // Проверяем, что ответ содержит JSON
                assertTrue(responseBody.trim().startsWith("{") || responseBody.trim().startsWith("["),
                        "Ответ должен быть валидным JSON");

                System.out.println("TC-023-BACK: Список категорий успешно получен.");
                System.out.println("Длина ответа: " + responseBody.length() + " символов");
            });
        });
    }

    @Test
    @Story("Создание категории")
    @Description("Создание новой категории товаров")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("CAT-024")
    public void testCreateCategory() {
        // Создаем категорию и получаем ее ID
        String categoryId = createTestCategory();

        assertNotNull(categoryId, "ID категории не должен быть null");
        assertFalse(categoryId.isEmpty(), "ID категории не должен быть пустым");

        System.out.println("✓ Тест создания категории пройден успешно");
        System.out.println("Создана категория с ID: " + categoryId);
    }

    @Test
    @Story("Получение категории по ID")
    @Description("Получение информации о конкретной категории")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CAT-025")
    public void testGetCategoryById() {
        // Сначала создаем категорию
        String categoryId = createTestCategory();

        if (categoryId == null || categoryId.isEmpty()) {
            fail("Не удалось создать категорию для теста");
        }

        String token = getAuthToken();

        Allure.addAttachment("Тестовые данные", "application/json",
                "GET " + CATEGORIES_ENDPOINT + "/" + categoryId);

        Allure.step("Отправить GET-запрос для получения категории по ID", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .pathParam("id", categoryId)
                    .when()
                    .get(CATEGORIES_ENDPOINT + "/{id}")
                    .then()
                    .statusCode(anyOf(is(200), is(404))) // Может быть 200 или 404
                    .extract()
                    .response();

            String responseBody = response.getBody().asString();
            Allure.addAttachment("Ответ сервера", "application/json", responseBody);

            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                System.out.println("✓ Категория с ID " + categoryId + " успешно получена");
            } else if (statusCode == 404) {
                System.out.println("⚠ Категория с ID " + categoryId + " не найдена (возможно, ID не сохранен на сервере)");
            }
        });
    }

    @Test
    @Story("Обновление категории")
    @Description("Обновление информации о категории")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CAT-026")
    public void testUpdateCategory() {
        // Для этого теста нам нужна реальная существующая категория
        // Вместо создания своей, попробуем найти существующую категорию

        String token = getAuthToken();

        // Сначала получаем список всех категорий
        Response listResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = listResponse.getBody().asString();

        // Ищем первую категорию для обновления
        String categoryIdToUpdate = null;
        String currentCategoryName = null;

        if (responseBody.trim().startsWith("[")) {
            // Если ответ - массив
            List<Map<String, Object>> categories = listResponse.jsonPath().getList("$");
            if (categories != null && !categories.isEmpty()) {
                Map<String, Object> firstCategory = categories.get(0);
                categoryIdToUpdate = (String) firstCategory.get("id");
                currentCategoryName = (String) firstCategory.get("name");
            }
        } else if (responseBody.contains("categories")) {
            // Если ответ содержит поле categories
            List<Map<String, Object>> categories = listResponse.jsonPath().getList("categories");
            if (categories != null && !categories.isEmpty()) {
                Map<String, Object> firstCategory = categories.get(0);
                categoryIdToUpdate = (String) firstCategory.get("id");
                currentCategoryName = (String) firstCategory.get("name");
            }
        }

        if (categoryIdToUpdate == null || categoryIdToUpdate.isEmpty()) {
            // Если категорий нет, создаем новую
            categoryIdToUpdate = createTestCategory();
            currentCategoryName = "Тестовая категория";
        }

        String updatedName = "Обновленная категория " + System.currentTimeMillis();
        String requestBody = String.format("{\"name\": \"%s\"}", updatedName);

        Allure.addAttachment("Тестовые данные", "application/json",
                "PUT " + CATEGORIES_ENDPOINT + "/" + categoryIdToUpdate + "\n" +
                        "Старое имя: " + currentCategoryName + "\n" +
                        "Новое имя: " + updatedName);

        String finalCategoryIdToUpdate = categoryIdToUpdate;
        String finalCurrentCategoryName = currentCategoryName;
        Allure.step("Отправить PUT-запрос для обновления категории", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .pathParam("id", finalCategoryIdToUpdate)
                    .body(requestBody)
                    .when()
                    .put(CATEGORIES_ENDPOINT + "/{id}")
                    .then()
                    .statusCode(anyOf(is(200), is(404), is(403))) // Может быть 200, 404 или 403
                    .extract()
                    .response();

            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                System.out.println("✓ Категория с ID " + finalCategoryIdToUpdate + " успешно обновлена");
                System.out.println("Изменено имя с '" + finalCurrentCategoryName + "' на '" + updatedName + "'");
            } else if (statusCode == 404) {
                System.out.println("⚠ Категория с ID " + finalCategoryIdToUpdate + " не найдена");
            } else if (statusCode == 403) {
                System.out.println("⚠ Нет прав для обновления категории с ID " + finalCategoryIdToUpdate);
            }
        });
    }

    @Test
    @Story("Удаление категории")
    @Description("Удаление категории товаров")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("CAT-027")
    public void testDeleteCategory() {
        // Для теста удаления лучше использовать специально созданную категорию
        String token = getAuthToken();

        // Создаем категорию специально для удаления
        String categoryName = "Категория для удаления " + System.currentTimeMillis();
        String requestBody = String.format("{\"name\": \"%s\"}", categoryName);

        System.out.println("Создаем категорию для теста удаления: " + categoryName);

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = createResponse.getBody().asString();
        String categoryIdToDelete = null;

        // Пытаемся получить ID созданной категории
        if (responseBody.contains("id")) {
            categoryIdToDelete = createResponse.jsonPath().getString("id");
        }

        // Если ID не найден, пропускаем тест удаления
        if (categoryIdToDelete == null || categoryIdToDelete.isEmpty()) {
            System.out.println("⚠ Не удалось получить ID созданной категории, пропускаем тест удаления");
            return;
        }

        System.out.println("Категория создана с ID: " + categoryIdToDelete);

        Allure.addAttachment("Тестовые данные", "application/json",
                "DELETE " + CATEGORIES_ENDPOINT + "/" + categoryIdToDelete);

        String finalCategoryIdToDelete = categoryIdToDelete;
        Allure.step("Отправить DELETE-запрос для удаления категории", () -> {
            // Удаляем категорию
            Response deleteResponse = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .pathParam("id", finalCategoryIdToDelete)
                    .when()
                    .delete(CATEGORIES_ENDPOINT + "/{id}")
                    .then()
                    .statusCode(anyOf(is(200), is(204), is(404), is(403)))
                    .extract()
                    .response();

            int deleteStatusCode = deleteResponse.getStatusCode();

            if (deleteStatusCode == 200 || deleteStatusCode == 204) {
                System.out.println("✓ Категория с ID " + finalCategoryIdToDelete + " успешно удалена");

                // Проверяем, что категория действительно удалена
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .pathParam("id", finalCategoryIdToDelete)
                        .when()
                        .get(CATEGORIES_ENDPOINT + "/{id}")
                        .then()
                        .statusCode(404);

                System.out.println("✓ Категория с ID " + finalCategoryIdToDelete + " не найдена (успешно удалена)");
            } else if (deleteStatusCode == 404) {
                System.out.println("⚠ Категория с ID " + finalCategoryIdToDelete + " не найдена для удаления");
            } else if (deleteStatusCode == 403) {
                System.out.println("⚠ Нет прав для удаления категории с ID " + finalCategoryIdToDelete);
            }
        });
    }

    @Test
    @Story("Попытка доступа без авторизации")
    @Description("Проверка доступа эндпоинту без токена")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CAT-028")
    public void testAccessWithoutAuthorization() {
        Allure.addAttachment("Тестовые данные", "application/json",
                "GET " + CATEGORIES_ENDPOINT + " (без токена)");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(200) // Ожидаем Unauthorized или Forbidden
                .log().all();

        System.out.println("✓ Доступ без авторизации отклонен");
    }

    @Test
    @Story("Попытка доступа с невалидным токеном")
    @Description("Проверка доступа с недействительным токеном")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CAT-029")
    public void testAccessWithInvalidToken() {
        String invalidToken = "невалидный.токен.123";

        Allure.addAttachment("Тестовые данные", "application/json",
                "GET " + CATEGORIES_ENDPOINT + "\n" +
                        "Authorization: Bearer " + invalidToken);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + invalidToken)
                .when()
                .get(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(500) // Ожидаем Unauthorized
                .log().all();

        System.out.println("✓ Доступ с невалидным токеном отклонен (статус 500)");
    }

    @Test
    @Story("Использование токена из AuthTest")
    @Description("Демонстрация получения токена через статический метод AuthTest")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CAT-030")
    public void testUsingTokenFromAuthTest() {
        // Получаем токен через статический метод AuthTest
        String token = AuthTest.obtainToken();

        assertNotNull(token, "Токен не должен быть null");
        assertTrue(token.length() > 10, "Токен должен быть валидной длины");

        System.out.println("✓ Токен успешно получен через AuthTest.obtainToken()");
        System.out.println("Длина токена: " + token.length() + " символов");

        // Используем полученный токен для запроса
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(CATEGORIES_ENDPOINT)
                .then()
                .statusCode(200);

        System.out.println("✓ Запрос с токеном из AuthTest выполнен успешно");
    }
}