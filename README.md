# Wallet Service

REST API для управления кошельками с поддержкой конкурентных операций (до 1000 RPS на один кошелёк).

## ✅ Возможности

- `POST /api/v1/wallet` — пополнение (`DEPOSIT`) или снятие (`WITHDRAW`)
- `GET /api/v1/wallets/{id}` — получение баланса
- Обработка ошибок: 400 (невалидный запрос), 404 (кошелёк не найден), 400 (недостаточно средств)
- Потокобезопасность через `PESSIMISTIC_WRITE` блокировку в PostgreSQL
- Миграции через Liquibase
- Запуск в Docker + docker-compose

## 🚀 Запуск

```bash
# Сборка (тесты можно пропустить)
mvn clean package -DskipTests

# Запуск
SERVER_PORT=8080 \
DB_PORT=5432 \
DB_NAME=walletdb \
DB_USER=postgres \
DB_PASS=postgres \
docker-compose up --build
```

Либо создайте .env
```env
SERVER_PORT=8080

DB_HOST=db
DB_PORT=5432
DB_NAME=walletdb
DB_USER=postgres
DB_PASS=mysecretpassword
```

И запустите просто с помощью
```bash
docker-compose up --build
```

Проверка работы
```bash
curl -X POST http://localhost:8080/api/v1/wallet \
  -H "Content-Type: application/json" \
  -d '{"valletId":"f47ac10b-58cc-4372-a567-0e02b2c3d479","operationType":"DEPOSIT","amount":100}'
```
