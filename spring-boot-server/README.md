# Running locally
1. Setup AWS Localstack profile first
    ```
    aws configure --profile localstack
    AWS Access Key ID [None]: dummy
    AWS Secret Access Key [None]: dummy
    Default region name [None]: eu-central-1
    Default output format [None]: json
    ```
   or create/update ~/.aws/credentials file manually with contents:
    ```
    [localstack]
    aws_access_key_id=dummy
    aws_secret_access_key=dummy
    ```

1. Start Localstack locally using docker-compose or podman-compose:
    ```
    cd docker-compose
    docker-compose up
    # or
    podman-compose up
    ```
1. Start the Spring Boot based application:
    ```
    mvn spring-boot:run -Dspring-boot.run.profiles=localdev
    ```
   or using env var:
   ```
   export SPRING_PROFILES_ACTIVE=localdev
   mvn spring-boot:run
   ```

# Running in the AWS environment

For the AWS-based run, follow the instructions at: https://pages.github.ibm.com/jAWS/ToDo-app

Please note, the spring boot profile used in the AWS account is `prod`.

