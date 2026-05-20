# Яндекс практикум. Мидл Java-разработчик. Модуль 3. Спринт 10

Микросервисное приложение Банк.
Добавляем развертывание приложения с помощью k8s и helm.

## Описание
Этот проект демонстрирует умение работать с
* Spring Boot Framework, Spring Boot Test, Spring WebFlux, Spring Data JPA, Spring Cloud, Spring Security, Lombok 
* микросервисами
* паттернами Single Service per Host, Database per Service, Gateway, Service Discovery, Circuit Breake, Load Balancing, External Configuration
* базами данных, в том числе встроенными (H2)
* модульным и интеграционным тестированием средствами Spring Boot Test + WebTestClient + JUnit5 + мокирование
* Maven, Git, Thymeleaf, Docker, Keycloak, Consul
* k8s, helm

## Требования
* Java JDK 21
* Maven
* PostgreSQL 17
* Docker
* Keycloack
* k8s
* Helm

## Названия и адреса модулей и сопровождающих сервисов
* Сервер авторизации OAuth 2.0: Keycloak (http://localhost:8080) 
* База данных: postgresql 17 (jdbc:postgresql://localhost:5432/postgres)
* Обноружение служб и управление конфигурациями: Hashicorp consul (http://localhost:8500)
* Фронт: bank-front (http://localhost:8081)
* Аккаунты: bank-accounts (http://localhost:8082)
* Обналичивание денег: bank-cash (http://localhost:8083)
* Перевод денег: bank-transfer (http://localhost:8084)
* Уведомления: bank-notifications (http://localhost:8085)
* Gateway API: bank-gateway (http://localhost:8086)

## Установка и запуск
1. **Клонируйте репозиторий:**
    ```bash
    git clone https://github.com/BorisoffEugene/bank-app
    ```
2. **Перейдите в папку проекта:**
    ```bash
    cd bank-app
    ```
3. **Соберите проект с помощью Maven:**
    ```bash
    mvn clean package
    ```
4. **Запустите тесты с помощью Maven:**
   ```bash
   mvn test
   ```
5. **Запуск микросервисов + Keycloak + postgres + consul:**
    ```bash
    docker-compose up --build
    ```

## Автор
**Борисов Евгений**
