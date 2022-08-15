import { defineConfig } from 'cypress'

export default defineConfig({
    pageLoadTimeout: 10000,
    responseTimeout: 5000,
    videosFolder: 'src/test/cypress/videos',
    screenshotsFolder: 'src/test/cypress/screenshots',
    e2e: {
        specPattern: "src/test/cypress/e2e/**/*.ts",
        supportFile: false,
        baseUrl: "http://localhost:8081"
    }
});
