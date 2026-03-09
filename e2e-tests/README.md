# E2E Tests - URL Shortener

End-to-end tests for the URL Shortener application using **Playwright**.

## Prerequisites

- Docker & Docker Compose
- Node.js 20+
- npm

## Quick Start

```bash
# From e2e-tests directory
docker-compose -f docker-compose.test.yml up

# In another terminal, run tests
npm install
npm run test
```

This starts:
- PostgreSQL database
- Redis cache
- Spring Boot application
- Nginx reverse proxy

## Running Tests

```bash
# Run all tests
npm run test

# Run in headed mode (see browser)
npx playwright test --headed
```

## Documentation

- [Main README](../README.md)
- [Development Setup](../DEVELOPMENT.md)

## Resources

- [Playwright Documentation](https://playwright.dev)
- [Best Practices](https://playwright.dev/docs/best-practices)
