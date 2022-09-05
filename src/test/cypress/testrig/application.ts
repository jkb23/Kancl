export function openMainPage() {
    cy.visit("/");
}

export function showsConnectedUser(name) {
    cy.get("p").should("contain", name);
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
