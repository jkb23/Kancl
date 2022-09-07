export function openMainPage() {
    cy.visit("/");
}

export function showsConnectedUser(name) {
    cy.get("p").should("contain", name);
}

export function showsLoggedUser(name) {
    cy.get(".user-info").should("contain", name);
}

export function showsEmptyMeeting() {
    cy.get("p").should("contain", "No participant");
}

export function postComment(author, message) {
    cy.get("#author").type("{selectall}{del}" + author);
    cy.get("#message").type("{selectall}{del}" + message);
    cy.get('#submit').click()
}

export function showsNoComments() {
    cy.get("#comments").children().should(comments => {
        expect(comments.length).to.equal(0);
    })
}

export function showsComment(index, author, message) {
    cy.get("#comments").children('li').eq(index).should(comment => {
        expect(comment.children('.author')).to.contain(author);
        expect(comment.children('.message')).to.contain(message);
    });
}

export function openCommentPage() {
    cy.visit("/comments");
}

export function openHelloPage() {
    cy.visit("/hello");
}

export function showsHello() {
    cy.get("h2").contains("Hello world");
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

export function checkVisibility() {
    cy.get('#submit').should('be.disabled')
}

export function checkNotificationCredentials() {
    cy.get('.invalid_credentials').contains('Invalid credentials')
}

export function checkLockoutCredentials() {
    cy.get('.invalid_credentials').contains('Too many unsuccessful attempts. Try again later.')
}

export function checkInvalidCredentialsFalse() {
    cy.get('.invalid_credentials').should('not.exist')
}

export function redirect() {
    cy.url().should('not.contains', '/login')
}

export function notRedirected() {
    cy.url().should('contains', '/login')
}

