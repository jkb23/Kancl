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
	cy.get("#comment").type("{selectall}{del}" + message);
}

export function showsComment(author, message) {
	cy.get("#messages").should("have.value", message)
}

export function openCommentPage() {
	cy.visit("/comment");
}

export function showsNoComments() {
	cy.get("#comments").should("have")
}