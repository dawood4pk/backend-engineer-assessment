# Getting Started

**This project involves a Java Spring application integrated with Stripe for payment processing and Temporal for workflow management. Follow the steps below to set up your development environment and run the application.**

## Setup Instructions

### Prerequisites

Ensure you have Java 17 or later, Docker, Temporal, and Stripe API keys.:

- [Java](https://www.azul.com/downloads/#zulu)
- [Temporal](https://docs.temporal.io/cli#install)
- [Docker](https://docs.docker.com/get-docker/)
- [Stripe API Keys](https://stripe.com/docs/keys)

### Configuration:
- Set up your Stripe API key in application.properties as:
    ```sh
    stripe.api-key=<your_stripe_secret_key>
    ```

### Running Services:
- Start the Temporal server:
    ```sh
    temporal server start-dev
    ```

- Start the application using:
    ```sh
    ./gradlew bootRun
    ```

### Running Tests
To run the suite of tests, use the following command:
```sh
./gradlew test
```
This will run unit and integration tests, ensuring all components interact correctly.


## Implementation Approach
The implementation is structured to use Spring Boot for the REST API, JPA for data persistence, and Temporal for managing long-running workflows. Stripe's Java library is used for payment processing.


## Assumptions
- Stripe and Temporal services are externally managed.
- The focus is on backend implementation with a RESTful approach.

## Code Walkthrough

For a complete walkthrough of the code, visit:

- [Video 1: - Exploring the project's local execution](https://www.loom.com/share/e34d7cdb143a48b78f9f6158cb4da58e?sid=1ec28465-cd98-43b0-b10a-13f624891570).
- [Video 2: - Exploring POST API Workflow and Implementation](https://www.loom.com/share/b38a78b5e4cf4aad8d116d9b13bf03dd?sid=fe38d7c2-5a07-4dbd-8516-057596d5334c).
- [Video 3: - Third Video Walkthrough](https://www.loom.com/share/940099f144c54f3fa3c2f9010d6bbf63?sid=f8c65652-c10b-4edd-9557-aa660d0dbd67).