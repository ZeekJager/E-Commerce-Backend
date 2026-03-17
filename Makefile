.PHONY: dev_up dev_down

dev_up:
	docker compose up --build

dev_down:
	docker compose down
