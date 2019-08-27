build:
	docker-compose -f docker/docker-compose.yml build sbe_user_build

test:
	docker network inspect sbe-network &> /dev/null || docker network create sbe-network
	docker-compose -f docker/docker-compose.yml run sbe_user_build bash -c "/usr/bin/mvn test && /usr/bin/mvn jacoco:report && /usr/bin/mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar" -f docker/docker-compose.yml

deploy:
	docker network inspect sbe-network &> /dev/null || docker network create sbe-network
	rm -rf target || true
	docker create --name temp_sbe_User_build docker_sbe_user_build
	docker cp temp_sbe_User_build:/app/target .
	docker rm -f temp_sbe_User_build
	docker-compose -f docker/docker-compose.yml up --build --detach sbe-user-deploy

stop:
	docker-compose -f docker/docker-compose.yml down
