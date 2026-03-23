.PHONY: dev_up dev_down build test logs ps clean

dev_up:
	docker compose up -d db
	./mvnw spring-boot:run

dev_down:
	docker compose down

build:
	./mvnw clean package -DskipTests

test:
	./mvnw test

logs:
	docker compose logs -f app

ps:
	docker compose ps

clean:
	./mvnw clean
	docker compose down -v --remove-orphans
