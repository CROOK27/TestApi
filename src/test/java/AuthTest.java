package Test;

import Test.dto.RequestLoginDto;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import Test.utils.TokenManager;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Authentication API")
@Feature("Управление аутентификацией")
@Owner("Максим")
public class AuthTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String AUTH_ENDPOINT = BASE_URL + "/auth/login";

    private String authToken;

    @BeforeAll
    @Step("Настройка базового URL")
    public static void setup() {
        baseURI = BASE_URL;
    }

    @Test
    @Story("Успешная авторизация")
    @Description("ID: TC-015-BACK - Успешная авторизация пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TC-015-BACK")
    public void testSuccessfulAuthorization() {
        // Предусловие: Пользователь создан в базе данных
        // Email – Chertolet@yamail.com, Password - tzarbomba

        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin("Chertolet@yamail.com");
        loginData.setPassword("tzarbomba");

        Allure.addAttachment("Тестовые данные", "application/json",
                "{\"login\": \"Chertolet@yamail.com\", \"password\": \"tzarbomba\"}");

        Allure.step("Отправить POST-запрос /auth/login с тестовыми данными", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(loginData)
                    .when()
                    .post(AUTH_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("token", notNullValue())
                    .body("token", not(emptyString()))
                    .body("$", hasKey("token")) // Проверяем наличие поля token
                    .body("$", not(hasKey("password"))) // Проверяем отсутствие пароля в ответе
                    .extract()
                    .response();

            String token = response.jsonPath().getString("token");
            this.authToken = token;

            // Сохраняем токен в менеджере для использования в других тестах
            TokenManager.saveDefaultToken(token);

            Allure.addAttachment("Успешный ответ", "application/json",
                    "Статус: 200 OK, Токен получен (первые 20 символов): " +
                            (token.length() > 20 ? token.substring(0, 20) + "..." : token));

            Allure.step("Проверка ответа", () -> {
                assertNotNull(token, "Токен не должен быть null");
                assertTrue(token.length() > 10, "Токен должен быть валидной длины");

                // Проверяем, что в ответе только поле token (опционально)
                Map<String, Object> responseMap = response.jsonPath().getMap("$");
                assertEquals(1, responseMap.size(), "В ответе должен быть только token");
                assertTrue(responseMap.containsKey("token"), "Ответ должен содержать поле token");
            });

            System.out.println("TC-015-BACK: Успешная авторизация. Получен токен.");
            System.out.println("Токен сохранен для использования в других тестах.");
        });
    }

    // Метод для получения токена из этого класса
    public String getAuthToken() {
        return authToken;
    }

    // Метод для принудительного получения нового токена
    @Step("Получить токен авторизации")
    public static String obtainToken() {
        // Если токен уже есть в менеджере, возвращаем его
        if (TokenManager.hasDefaultToken()) {
            return TokenManager.getDefaultToken();
        }

        // Иначе выполняем авторизацию
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
        return token;
    }

    @Test
    @Story("Неверная авторизация")
    @Description("ID: TC-016-BACK - Неверная авторизация пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-016-BACK")
    public void testFailedAuthorizationInvalidPassword() {
        // Предусловие: Пользователь создан в базе данных

        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin("Chertolet@yamail.com");
        loginData.setPassword("cantsign");

        Allure.addAttachment("Тестовые данные", "application/json",
                "{\"login\": \"Chertolet@yamail.com\", \"password\": \"cantsign\"}");

        Allure.step("Отправить POST-запрос /auth/login с указанными тестовыми данными", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(loginData)
                    .when()
                    .post(AUTH_ENDPOINT)
                    .then()
                    .statusCode(401)
                    .extract()
                    .response();

            String responseBody = response.getBody().asString();
            Allure.addAttachment("Ответ сервера", "application/json",
                    "Status: " + response.getStatusCode() + ", Body: " + responseBody);

            Allure.step("Проверка ожидаемого результата", () -> {
                assertEquals(401, response.getStatusCode(), "Статус код должен быть 401");

                // Проверяем наличие поля error в ответе
                if (responseBody.contains("error")) {
                    assertTrue(responseBody.contains("Invalid credentials") ||
                                    responseBody.toLowerCase().contains("invalid") ||
                                    responseBody.toLowerCase().contains("unauthorized"),
                            "Ответ должен содержать сообщение об ошибке");
                }

                // Проверяем, что токен не выдан
                assertFalse(responseBody.contains("token"), "Токен не должен быть выдан");
                assertFalse(responseBody.contains("access_token"), "Access token не должен быть выдан");
            });

            System.out.println("TC-016-BACK: Неверная авторизация. Статус 401 получен.");
        });
    }

    @Test
    @Story("Авторизация без обязательных полей")
    @Description("ID: TC-017-BACK - Авторизация без обязательных полей")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-017-BACK")
    public void testAuthorizationWithoutRequiredFields() {
        // Предусловие: Пользователь создан в базе данных

        // Создаем запрос только с email, без password
        Map<String, String> incompleteData = new HashMap<>();
        incompleteData.put("login", "Chertolet@yamail.com");
        // Поле password отсутствует

        Allure.addAttachment("Тестовые данные", "application/json",
                "{\"login\": \"Chertolet@yamail.com\"}");

        Allure.step("Отправить POST-запрос с указанными тестовыми данными", () -> {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(incompleteData)
                    .when()
                    .post(AUTH_ENDPOINT)
                    .then()
                    .statusCode(401)
                    .extract()
                    .response();

            String responseBody = response.getBody().asString();
            Allure.addAttachment("Ответ сервера", "application/json",
                    "Status: " + response.getStatusCode() + ", Body: " + responseBody);

            Allure.step("Проверка ожидаемого результата", () -> {
                assertEquals(401, response.getStatusCode(), "Статус код должен быть 401");

                // Проверяем наличие сообщения об ошибке для поля password
                if (responseBody.contains("password") || responseBody.contains("Password")) {
                    assertTrue(responseBody.toLowerCase().contains("required") ||
                                    responseBody.contains("Field is required") ||
                                    responseBody.contains("обязательно"),
                            "Ответ должен указывать на обязательность поля password");
                }

                // Проверяем, что токен не выдан
                assertFalse(responseBody.contains("token"), "Токен не должен быть выдан");
            });

            System.out.println("TC-017-BACK: Авторизация без обязательных полей. Статус 401 получен.");
        });
    }

    @Test
    @Story("Авторизация с пустыми полями")
    @Description("Дополнительный тест: Авторизация с пустыми логином и паролем")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("AUTH-018")
    public void testAuthorizationWithEmptyFields() {
        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin("");
        loginData.setPassword("");

        Allure.addAttachment("Тестовые данные", "application/json",
                "{\"login\": \"\", \"password\": \"\"}");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(401)
                .extract()
                .response();

        Allure.addAttachment("Ответ", "application/json",
                "Status: " + response.getStatusCode());

        System.out.println("Дополнительный тест: Авторизация с пустыми полями. Статус: " + response.getStatusCode());
    }

    @Test
    @Story("Авторизация несуществующего пользователя")
    @Description("Дополнительный тест: Попытка авторизации несуществующего пользователя")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("AUTH-019")
    public void testAuthorizationNonExistentUser() {
        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin("nonexistent@example.com");
        loginData.setPassword("somepassword");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(401)
                .extract()
                .response();

        Allure.addAttachment("Ответ", "application/json",
                "Status: " + response.getStatusCode() + " - Пользователь не найден");

        System.out.println("Дополнительный тест: Авторизация несуществующего пользователя. Статус 401.");
    }

    @ParameterizedTest
    @CsvSource({
            "Chertolet@yamail.com, '', 401", // Пустой пароль
            "'', tzarbomba, 401", // Пустой логин
            "wrongemail@example.com, tzarbomba, 401", // Неверный email
            "Chertolet@yamail.com, wrongpassword, 401" // Неверный пароль
    })
    @Story("Параметризованные тесты авторизации")
    @Description("Проверка различных комбинаций логина и пароля")
    @Severity(SeverityLevel.NORMAL)
    void testParameterizedAuthorization(String login, String password, int expectedStatusCode) {
        RequestLoginDto loginData = new RequestLoginDto();
        loginData.setLogin(login);
        loginData.setPassword(password);

        given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode);

        System.out.println("Параметризованный тест: login=" + login +
                ", expectedStatus=" + expectedStatusCode);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }
}