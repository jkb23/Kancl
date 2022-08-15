export function openMainPage() {
	cy.visit("/");
}

export function showsConnectedUser(name) {
	cy.get("p").should("contain", name);
}

export function showsEmptyMeeting() {
	cy.get("p").should("contain", "No participant");
}

export function postComment(author, comment) {
	cy.get("#author").type("{selectall}{del}" + author);
	cy.get("#comment").type("{selectall}{del}" + comment);
}

export function showsComment(expectedValue) {
	cy.get("#messages").should("have.value", expectedValue)
}
