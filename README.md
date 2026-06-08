# Reelify — Backend

REST API built with Spring Boot and MongoDB for the Reelify movie app.

---

## Tech Stack

| Technology | Version | Role |
|-----------|---------|------|
| Java | 21 | Language |
| Spring Boot | 3.4.5 | Framework |
| Spring Data MongoDB | — | Database layer |
| MongoDB Atlas | M0 (free) | Database |
| Lombok | — | Boilerplate reduction |
| Docker | — | Containerization |
| Render | — | Hosting |

---

## Project Structure

```
src/
└── main/
    └── java/dev/sebastien/movies/
        ├── MoviesApplication.java      # Entry point
        ├── CorsConfig.java             # CORS configuration
        ├── Movie.java                  # Movie model
        ├── MovieController.java        # Movie REST endpoints
        ├── MovieService.java           # Movie business logic
        ├── MovieRepository.java        # Movie MongoDB repository
        ├── Review.java                 # Review model
        ├── ReviewController.java       # Review REST endpoints
        ├── ReviewService.java          # Review business logic
        └── ReviewRepository.java       # Review MongoDB repository
```

---

## API Endpoints

### Movies

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/movies` | Get all movies |
| GET | `/api/v1/movies/{imdbId}` | Get a single movie |
| GET | `/api/v1/movies/genre/{genre}` | Get movies by genre |
| POST | `/api/v1/movies` | Create a movie |
| PUT | `/api/v1/movies/{imdbId}` | Update a movie |
| DELETE | `/api/v1/movies/{imdbId}` | Delete a movie |

### Reviews

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/reviews` | Add a review to a movie |

---

## Movie Model

```json
{
  "imdbId": "tt11116912",
  "title": "Troll",
  "trailerLink": "https://www.youtube.com/watch?v=AiohkY_XQYQ",
  "genres": ["Fantasy", "Action", "Adventure"],
  "poster": "https://image.tmdb.org/t/p/w500/...",
  "backdrops": ["https://image.tmdb.org/t/p/original/..."],
  "reviewIds": []
}
```

## Review Model

```json
{
  "reviewBody": "Great movie!",
  "imdbId": "tt11116912"
}
```

---

## Local Setup

### Prerequisites

- Java 21
- Maven
- MongoDB Atlas account (or local MongoDB)

### 1. Clone the repo

```bash
git clone https://github.com/your-username/movies-backend.git
cd movies-backend
```

### 2. Create a `.env` file at the root

```env
MONGO_USER=your_atlas_username
MONGO_PASSWORD=your_atlas_password
MONGO_CLUSTER=cluster0.xxxxx.mongodb.net
MONGO_DATABASE=movies
```

### 3. Run the app

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 4. Test an endpoint

```bash
curl http://localhost:8080/api/v1/movies
```

---

## Docker

### Build and run locally with Docker

```bash
docker build -t movies-backend .
docker run -p 8080:8080 \
  -e MONGO_USER=your_user \
  -e MONGO_PASSWORD=your_password \
  -e MONGO_CLUSTER=cluster0.xxxxx.mongodb.net \
  -e MONGO_DATABASE=movies \
  movies-backend
```

### Dockerfile overview

```dockerfile
# Stage 1 — Build the jar with Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2 — Run the jar with a lightweight JRE
FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

---

## Deployment on Render

### Prerequisites

- A GitHub repo with the project pushed
- A MongoDB Atlas cluster with `0.0.0.0/0` in the IP Access List

### Steps

1. Go to [render.com](https://render.com) → **New** → **Web Service**
2. Connect your GitHub repo
3. Set runtime to **Docker**
4. Add the following environment variables:

| Key | Value |
|-----|-------|
| `MONGO_USER` | Your Atlas username |
| `MONGO_PASSWORD` | Your Atlas password |
| `MONGO_CLUSTER` | `cluster0.xxxxx.mongodb.net` |
| `MONGO_DATABASE` | `movies` |

5. Click **Create Web Service** — Render builds and deploys automatically

### Live URL format

```
https://movies-backend-xxxx.onrender.com
```

> Note: the free tier on Render sleeps after 15 minutes of inactivity. The first request after sleep takes ~30 seconds to respond.

---

## CORS Configuration

The backend allows requests from the following origins:

```java
"http://localhost:4200"       // Local Angular dev server
"https://your-app.vercel.app" // Production frontend
```

Update `CorsConfig.java` with your actual Vercel URL after deploying the frontend.

---

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `MONGO_USER` | MongoDB Atlas username | `myuser` |
| `MONGO_PASSWORD` | MongoDB Atlas password | `mypassword` |
| `MONGO_CLUSTER` | Atlas cluster host | `cluster0.abc12.mongodb.net` |
| `MONGO_DATABASE` | Database name | `movies` |
