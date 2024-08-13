# CI/CD Pipeline for Spring Boot Application with Docker and GitHub Actions

This project demonstrates a CI/CD pipeline for deploying a Spring Boot application using GitHub Actions, Docker, and an Amazon EC2 instance. The pipeline includes the following stages:

1. **Build Job**: This job builds a Docker image for the Spring Boot application and pushes it to Docker Hub.
2. **Deploy Job**: This job deploys the Docker image from Docker Hub to an EC2 instance using a self-hosted GitHub Actions runner.

## Prerequisites

- **GitHub Account**: Ensure you have a GitHub account with the repository set up for your Spring Boot application.
- **Docker Hub Account**: A Docker Hub account is required to store the Docker image.
- **AWS Account**: An AWS account is needed to create and manage the EC2 instance.

## Project Structure

- **Spring Boot Application**: The main application is built using Spring Boot and Java 17.
- **Docker**: The application is containerized using Docker, with the Dockerfile provided in the root directory of the project.

## Pipeline Overview

### 1. Build Job
- **Trigger**: The job is triggered when changes are pushed to the GitHub repository.
- **Steps**:
  1. Checkout the code from the repository.
  2. Build the Docker image for the Spring Boot application.
  3. Push the Docker image to Docker Hub.

### 2. Deploy Job
- **Trigger**: The deploy job is triggered after the successful completion of the build job.
- **Steps**:
  1. Use a self-hosted runner on the EC2 instance.
  2. Pull the Docker image from Docker Hub.
  3. Run the Docker image as a container on the EC2 instance.

## Getting Started

### 1. Setting Up the GitHub Repository

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/your-username/your-repo.git
   cd your-repo
