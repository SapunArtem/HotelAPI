Hotel Management API
====================

RESTful API для управления информацией об отелях. Приложение предоставляет возможность просматривать, искать, создавать отели и управлять их удобствами.

Технологический стек
--------------------

-   Java 21

-   Spring Boot 4.0.3

-   Spring Data JPA

-   Spring Web MVC

-   Spring Validation

-   Liquibase

-   H2 Database

-   MySQL (поддерживается)

-   Lombok

-   SpringDoc OpenAPI (Swagger)

-   Maven

Функциональность
----------------

API предоставляет следующие endpoints (все с префиксом `/property-view`):

### 1\. Получение списка отелей

```
GET /hotels
```

Возвращает список всех отелей с краткой информацией.

Пример ответа:

```

[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375 17 309-80-00"
  }
]
```

### 2\. Получение детальной информации об отеле

```
GET /hotels/{id}
```

Возвращает расширенную информацию о конкретном отеле.

Пример ответа:

```
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  },
  "amenities": [
    "Free parking",
    "Free WiFi",
    "Non-smoking rooms",
    "Concierge",
    "On-site restaurant",
    "Fitness center",
    "Pet-friendly rooms",
    "Room service",
    "Business center",
    "Meeting rooms"
  ]
}
```

### 3\. Поиск отелей

```
GET /search
```

Поиск отелей по параметрам: name, brand, city, country, amenities.

Пример: `/search?city=Minsk&amenities=Free WiFi`

### 4\. Создание нового отеля

```
POST /hotels
```

Создает новый отель на основе переданных данных.

Пример запроса:


```
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```

### 5\. Добавление удобств к отелю

```
POST /hotels/{id}/amenities
```

Добавляет список удобств к указанному отелю.

Пример запроса:

```

[
  "Free parking",
  "Free WiFi",
  "Non-smoking rooms",
  "Concierge",
  "On-site restaurant",
  "Fitness center",
  "Pet-friendly rooms",
  "Room service",
  "Business center",
  "Meeting rooms"
]
```

### 6\. Получение гистограммы

```
GET /histogram/{param}
```

Возвращает количество отелей, сгруппированных по указанному параметру (brand, city, country, amenities).

Пример для `/histogram/city`:

```
{
  "Minsk": 1,
  "Moscow": 2,
  "Mogilev": 1
}
```

Пример для `/histogram/amenities`:


```
{
  "Free parking": 1,
  "Free WiFi": 20,
  "Non-smoking rooms": 5,
  "Fitness center": 1
}
```

Запуск приложения
-----------------

### Требования

-   JDK 21

-   Maven 3.6+

### Запуск с H2 (по умолчанию)
```
mvn spring-boot:run
```

### Запуск с MySQL
```
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### Сборка проекта

```
mvn clean package
```

Конфигурация
------------

### Порт и контекстный путь

-   Порт: 8092

-   Контекстный путь: `/property-view`

### Профили

#### H2 (default)

-   База данных: in-memory H2

-   Консоль H2: <http://localhost:8092/property-view/h2-console>

-   JDBC URL: `jdbc:h2:mem:hoteldb`

-   Username: `sa`

-   Password: `password`

#### MySQL

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hoteldb
    username: root
    password: password

Документация API
----------------

После запуска приложения документация Swagger UI доступна по адресу:


```
http://localhost:8092/property-view/swagger-ui.html
```

OpenAPI спецификация:

```
http://localhost:8092/property-view/api-docs
```


База данных
-----------

### Схема данных

-   hotels - основная таблица отелей

-   amenities - справочник удобств

-   hotel_amenities - связь многие-ко-многим между отелями и удобствами

### Миграции

Liquibase автоматически создает и обновляет схему базы данных при запуске приложения. Миграции находятся в `src/main/resources/db/changelog/`.

Тестирование
------------

### Запуск тестов

```
mvn test
```

### Виды тестов

-   Модульные тесты - тестирование отдельных компонентов

-   Интеграционные тесты - тестирование взаимодействия компонентов

-   Репозитории - тестирование запросов к БД

-   Контроллеры - тестирование REST endpoints

Возможные улучшения
-------------------

-   Добавить пагинацию для списка отелей

-   Реализовать кэширование

-   Добавить больше тестов

-   Docker контейнеризация

-   CI/CD пайплайн (GitHub Actions)

-   Аутентификация и авторизация

-   Валидация форматов email и телефона

-   Добавить возможность загрузки изображений отелей

Известные проблемы и решения
----------------------------

### Проблема: Тесты не запускаются

Решение: Убедитесь, что класс `Application.java` существует в пакете `org.example`

### Проблема: Lombok не работает в тестах

Решение: Проверьте настройки компилятора и annotation processing в IDEA

Вклад в проект
--------------

1.  Форкните репозиторий

2.  Создайте ветку для новой функциональности

3.  Внесите изменения

4.  Напишите тесты

5.  Отправьте pull request

Лицензия
--------

Apache 2.0

Контакты
--------

-   Автор: Sapun Artem

-   GitHub: [SapunArtem](https://github.com/SapunArtem)
