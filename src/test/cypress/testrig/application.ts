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

// Registration Page

export function openRegistrationPage() {
    cy.visit("/register");
}

export function enterCredentialsRegistration(username, email, password, passwordCheck) {
    cy.get("#username").type("{selectall}{del}" + username);
    cy.get("#email").type("{selectall}{del}" + email);
    cy.get("#password").type("{selectall}{del}" + password);
    cy.get("#passwordCheck").type("{selectall}{del}" + passwordCheck);
}


export function doesNotDisplayRegistrationPage() {
    cy.url().should('not.contains', '/register');
}

export function displaysRegistrationPage() {
    cy.url().should('contains', '/register');
}

export function hasErrorMessage() {
    cy.get('.invalid_credentials').contains('exists');
}