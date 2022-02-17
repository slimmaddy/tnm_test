1. Solution
- Using Spring Batch and Spring Integration for integration daemon
- After loading job done, integration daemon  sends notification to RabbitMQ
- Application service receives notification then process task assignment business
- Application service exposes API for client
2. Environment
- docker-compose: version > 1.27.3 (support relative path in driver-opts in docker-compose file) https://github.com/docker/compose/pull/7762
3. Run
- clear data in following directory: `db, data, queue, logs`
- Run _`docker-compose up -d`_
- because services depend on mysql and rabbitmq, sometimes services start failed due to lately start up mysql/rabbitmq. Just rerun `docker-compose up -d`
- copy content from `./java_assignment_log` dir to `./data` dir
5. Link
- WebUI: http://localhost:8080/web/index.html
- SwaggerUI: http://localhost:8080/swagger-ui/index.html
- API: http://localhost:8080/team-assignment