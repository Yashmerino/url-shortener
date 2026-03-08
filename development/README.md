# 🚀 URL Shortener - Development Environment

This project provides a containerized development environment designed for high performance and seamless developer experience, featuring **Hot Reload**, **Reverse Proxy**, and infrastructure services.

## 💻 Prerequisites

- Docker and Docker Compose installed
- IDE (IntelliJ IDEA recommended) with debugger support

## 🛠️ Quick Start

### Start the entire stack
From the `development/` directory, run:

```bash
docker-compose up --build
```

This will:
- Build the Java Spring Boot application
- Start PostgreSQL (port 5432)
- Start Redis (port 6379)
- Start Nginx reverse proxy (port 80)
- Start the URL Shortener app (port 8080, exposed via Nginx)

### Stop and Cleanup

```bash
# Stop containers (data persists)
docker-compose down

# Stop containers and DELETE all database data (full reset)
docker-compose down -v
```

## 🔥 Hot Reload

The development environment is configured with **Spring DevTools** for automatic reloading:

### How it works

When you modify Java source files or resources:
1. Changes are automatically compiled
2. Spring DevTools detects the changes and restarts the application context
3. The browser automatically reloads (via LiveReload)

### File watching

- **Java files** (`src/main/java/**`): Automatic restart and recompilation
- **Template files** (`src/main/resources/templates/**`): Automatic reload without restart
- **Properties files**: Automatic restart

### LiveReload

To enable automatic browser refresh on file changes, add the **LiveReload** browser extension:
- Chrome: [LiveReload extension](https://chromewebstore.google.com/detail/livereload/jnihajbhpnppcjjlhcifea/)
- Firefox: [LiveReload extension](https://addons.mozilla.org/en-US/firefox/addon/livereload/)

After adding the extension, click the extension icon to enable LiveReload for the current tab.
## 🐛 Remote Debugging

### Enable Remote Debugging in IntelliJ IDEA

#### 1. Configure the Debug Configuration
- Go to **Run** → **Edit Configurations**
- Click **+** and select **Remote JVM Debug**
- Configure:
  - **Name**: `URL Shortener - Remote Debug`
  - **Host**: `localhost`
  - **Port**: `5005`
  - **Debugger mode**: `Attach to process`

#### 2. Start Debugging
- Ensure the container is running: `docker-compose up -d`
- Click **Debug** (or press `Shift+F9`)
- Set breakpoints in your IDE

#### 3. Trigger Your Code
- Use the application at `http://localhost`
- The debugger will pause at breakpoints

## 📊 Infrastructure Services

### PostgreSQL
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `url_shortener`
- **User**: `user`
- **Password**: `123`

Connect using any PostgreSQL client (e.g., DBeaver, pgAdmin):
```
postgresql://user:123@localhost:5432/url_shortener
```

### Redis
- **Host**: `localhost`
- **Port**: `6379`

Connect using Redis CLI:
```bash
redis-cli -h localhost -p 6379
```

## 🌐 Accessing the Application

- **Application**: `http://localhost` (redirected via Nginx)
- **Spring Boot app**: `http://localhost:8080`
- **OpenAPI/Swagger**: `http://localhost:8080/swagger-ui.html`
- **Health Check**: `http://localhost:8080/actuator/health`

## 💡 Development Tips

### Restarting the Application
If hot reload doesn't work or you need a full restart:
```bash
docker-compose restart url-shortener
```

### Database Migrations
Migrations are automatically applied using Flyway on startup:
- Location: `src/main/resources/db/migration/`
- Migration files: `V*__*.sql` (e.g., `V1__Initial_schema.sql`)