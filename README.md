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

For a complete walkthrough of the code, visit [Video Walkthrough](https://tobeadded.com/).
