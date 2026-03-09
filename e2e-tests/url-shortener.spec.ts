import { test, expect } from '@playwright/test';

const BASE_URL = process.env.PLAYWRIGHT_TEST_BASE_URL || 'http://localhost';

test.describe('URL Shortener E2E Tests', () => {

  test('create link and redirect', async ({ page }) => {
    const originalUrl = 'https://www.google.com';
    await page.goto(BASE_URL);

    await page.fill('input[name="originalUrl"]', originalUrl);
    await page.click('button[type="submit"]');

    const resultLink = page.locator('#shortUrlResult'); 
    await expect(resultLink).toBeVisible();
    
    const shortUrl = await resultLink.inputValue();
    await page.goto(shortUrl!);

    await expect(page).toHaveURL(/google.com/);
  });

  test('should redirect to 404 error page for invalid URL', async ({ page }) => {
    await page.goto(BASE_URL + '/invalid-short-link');

    await expect(page).toHaveURL(BASE_URL + '/invalid-short-link');
    const notFoundHeader = page.locator('h1');
    await expect(notFoundHeader).toContainText('404');
  });

  test('should work even if the link is accessed multiple times', async ({ page }) => {
    const originalUrl = 'https://www.wikipedia.org';
    
    await page.goto(BASE_URL);
    await page.fill('input[name="originalUrl"]', originalUrl);
    await page.click('button[type="submit"]');
    const shortUrl = await page.locator('#shortUrlResult').inputValue();

    for (let i = 0; i < 3; i++) {
        await page.goto(shortUrl!);
        await expect(page).toHaveURL(/wikipedia.org/);
    }
  });
});