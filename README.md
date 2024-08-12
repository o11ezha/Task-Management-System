# Task-Management-System

## Описание
Данное тестовое API предназначено для управления задачами. 
Пользователь может регистрироваться и аутентифицироваться, создавать, редактировать, назначать и удалять задачи (в том числе вывод задач можно фильтровать при помощи фильтрации и пагинации). 
К задачам можно оставлять и удалять комментарии.

## Docker
> ❗️ Внимание❗ ️
>
> Перед тем, как запускать проект в docker, убедитесь, что у Вас не заняты порты **8080** и **5432**.
> Если порты заняты, измените их в файле `docker-compose.yml` в разделе `services` -> `app` -> `ports` и `services` -> `db` -> `ports`.

Для того, чтобы поднять проект в docker, вам нужно:

1. Склонировать репозиторий
```bash
git clone https://github.com/o11ezha/Task-Management-System.git
```

2. Перейти в папку проекта (Task-Management-System\\**ControlPanel**)

3. Выполнить команду `./mvnw package -DskipTests` 
![Запуск команды](https://github.com/o11ezha/Task-Management-System/raw/assets/ControlPanel/assets/Снимок%20экрана%202024-08-12%20194525.png)
После этого у Вас должна появиться папка `target` в корне проекта.

4. Создать файл `.env` в корне проекта и добавить в него следующие переменные:
```
DB_USERNAME=<ЗдесьВашеЗначение> # Имя пользователя для подключения к БД
DB_PASSWORD=<ЗдесьВашеЗначение> # Пароль для подключения к БД
DB_HOST=db # Хост для подключения к БД
DB_PORT=5432 # Порт для подключения к БД
DB_NAME=<ЗдесьВашеЗначение> # Имя БД
TOKEN_SECRET=<ЗдесьВашеЗначение> # Секретный ключ для генерации токенов
TOKEN_TIME_TO_LIVE=86400000 # Время жизни токена в миллисекундах
```
![Файл .env](https://github.com/o11ezha/Task-Management-System/raw/assets/ControlPanel/assets/Снимок%20экрана%202024-08-12%20195248.png)

5. Выполнить команду:
```bash
docker-compose up --build
```
После этого у Вас поднимутся два контейнера (внизу приведён скрин из Docker Desktop):
![Скрин из Docker Desktop с двумя контейнерами](https://github.com/o11ezha/Task-Management-System/raw/assets/ControlPanel/assets/Снимок%20экрана%202024-08-12%20195433.png)

6. API будет доступно по адресу `http://localhost:8080/api/`

7. Документация к API будет доступна по адресу `http://localhost:8080/api/swagger-ui/index.html`

> ❗️ Внимание❗ ️
>
> В Swagger учтено две группы: неавторизованные пользователи и авторизованные пользователи (no-auth и authenticated соотвтственно).
> Для того, чтобы получить доступ к методам, требующим авторизации, необходимо авторизоваться при помощи кнопки `Authorize` и вставить JWT-токен в поле `Value` в Swagger.
> JWT-токен можно получить при помощи метода `POST /v1/auth/login`, если Вы зарегистрировались при помощи метода `POST /v1/auth/register`.

## Используемые технологии
- Java 21
- Spring Boot 3.3.2
- Spring Security
- Spring Web
- Spring Validation
- Spring Boot Hateoas
- Spring Boot Data Rest
- Spring Boot Starter Test
- Maven
- JWT
- PostgreSQL
- Tika
- Spring Data JPA
- Lombok
- MapStruct
- Swagger
- Docker
- Git
