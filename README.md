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
![Запуск команды](https://www.dropbox.com/scl/fi/0sia9fcxpvktkf5rdn6o6/2024-08-12-194525.png?rlkey=r6e6w3vubwrzr92b53k6gef2q&st=oywpj13e&dl=1)
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
![Файл .env](https://www.dropbox.com/scl/fi/dezj1b1dh9nwudadu85w6/2024-08-12-195248.png?rlkey=8muzzpu1shi23qb43plvgxls2&st=eild13e7&dl=1)
5. Выполнить команду:
```bash
docker-compose up --build
```
После этого у Вас поднимутся два контейнера (внизу приведён скрин из Docker Desktop):
![Скрин из Docker Desktop с двумя контейнерами](https://www.dropbox.com/scl/fi/gl33f5sbyfg9yf3mdqfsc/2024-08-12-195433.png?rlkey=28uvoubfk5jxkyecjxvgt9vtd&st=v1nj0s45&dl=1)
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