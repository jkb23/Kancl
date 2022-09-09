export function openMainPage() {
    cy.visit("/");
}

export function showsLoggedUser(name) {
    cy.get(".user-info").should("contain", name);
}

export function recreateDatabase() {
    cy.visit("/recreateDb");
}

export function openLoginPage() {
    cy.visit("/login");
}

export function enterCredentials(username, password) {
    cy.get("#username").type("{selectall}{del}" + username);
    cy.get("#password").type("{selectall}{del}" + password);
}

export function clickSubmit() {
    cy.get('#submit').click();
}

export function submitButtonIsDisabled() {
    cy.get('#submit').should('be.disabled');
}

export function displaysInvalidCredentials() {
    cy.get('.invalid_credentials').contains('Invalid credentials');
}

export function displaysTooManyUnsuccessfulLoginAttempts() {
    cy.get('.invalid_credentials').contains('Too many unsuccessful attempts. Try again later.');
}

export function doesNotShowInvalidCredentials() {
    cy.get('.invalid_credentials').should('not.exist');
}

export function doesNotDisplayLoginPage() {
    cy.url().should('not.contains', '/login');
}

export function displaysLoginPage() {
    cy.url().should('contains', '/login');
}
