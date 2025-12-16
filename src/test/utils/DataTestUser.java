package utils;

import ru.rtc.medline.application.domain.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataTestUser {
    public void insertUsersIntoDatabase() {
        String url = "jdbc:postgresql://localhost:5432/student_store";
        String user = "postgres";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Получаем SQL строку
            String sql = getUsersAsSQL();

            // Выполняем SQL
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Добавлено " + rowsAffected + " пользователей");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<User> testUsers = new ArrayList<>();

    private enum UserRole {
        ADMIN("14ccdfc9-5682-4152-8c96-f9e6cb7cd473"),
        USER("b182851a-b655-4786-ae3e-b19d20623d09");

        private final String roleId;

        UserRole(String roleId) {
            this.roleId = roleId;
        }

        public String getRoleId() {
            return roleId;
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private User createUser(String id, String phone, String email,
                            String firstName, String lastName, String middleName,
                            LocalDate birthDate, String password, String roleId,
                            String username) {
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setBirthDate(birthDate);
        user.setPassword(password);

        user.setUsername(username);

        return user;
    }

    public List<User> createTwoTestUsers() {
        // Очистка предыдущих данных
        testUsers.clear();

        // 1. Администратор
        testUsers.add(createUser(
                generateId(),
                "79001234567",
                "admin@medline.ru",
                "Алексей",
                "Иванов",
                "Петрович",
                LocalDate.of(1980, 5, 15),
                "$2a$10$N9qo8uLOickgx2ZMRZoMye.M.6hJYHA6QicAHYpFhK6v5JYQ9Q1/W",
                UserRole.ADMIN.getRoleId(),  // roleId как строковый параметр
                "admin"
        ));

        // 2. Обычный пользователь
        testUsers.add(createUser(
                generateId(),
                "88005553535",
                "Chertolet@yamail.com",
                "Олег",
                "Абрамов",
                "Николаевич",
                LocalDate.of(1904, 4, 10),
                "tzarbomba",
                UserRole.USER.getRoleId(),  // roleId как строковый параметр
                "user"
        ));

        System.out.println("Создано 2 тестовых пользователя:");
        System.out.println("1. Администратор (role_id=1)");
        System.out.println("2. Пользователь (role_id=2)");

        return testUsers;
    }


    public User getUser(int index) {
        if (testUsers.isEmpty()) {
            createTwoTestUsers();
        }

        if (index >= 0 && index < testUsers.size()) {
            return testUsers.get(index);
        }
        return null;
    }


    public User getAdminUser() {
        return getUser(0);
    }


    public User getRegularUser() {
        return getUser(1);
    }


    public String getUsersAsSQL() {
        if (testUsers.isEmpty()) {
            createTwoTestUsers();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO users (id, phone, email, first_name, last_name, middle_name, birth_date, password, role_id) VALUES\n");

        for (int i = 0; i < testUsers.size(); i++) {
            User user = testUsers.get(i);

            sql.append("  ('")
                    .append(user.getId()).append("', '")
                    .append(user.getPhone()).append("', '")
                    .append(user.getEmail()).append("', '")
                    .append(user.getFirstName()).append("', '")
                    .append(user.getLastName()).append("', ")
                    .append(user.getMiddleName() != null ? "'" + user.getMiddleName() + "'" : "NULL").append(", '")
                    .append(user.getBirthDate()).append("', '")
                    .append(user.getPassword()).append("', '")
                    .append(i == 0 ? UserRole.ADMIN.getRoleId() : UserRole.USER.getRoleId()) // Используем UUID из enum
                    .append("')");

            if (i < testUsers.size() - 1) {
                sql.append(",\n");
            } else {
                sql.append(";\n");
            }
        }

        return sql.toString();
    }

    public static void main(String[] args) {
        DataTestUser dataTest = new DataTestUser();

        // Создаем пользователей
        List<User> users = dataTest.createTwoTestUsers();

        // Показываем SQL
        System.out.println("SQL запрос:");
        System.out.println(dataTest.getUsersAsSQL());

        // Вставляем в БД (раскомментируйте когда настроите подключение)
        dataTest.insertUsersIntoDatabase();
    }
}