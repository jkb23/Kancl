export function openMainPage() {
	cy.visit("/");
}

export function showsConnectedUser(name) {
	cy.get("p").should("contain", name);
}

export function showsEmptyMeeting() {
	cy.get("p").should("contain", "No participant")
}

export function setTestFieldValue(value) {
	cy.get("#testField").type("{selectall}{del}" + value)
}

export function showsTestFieldWithValue(expectedValue) {
	cy.get("#testField").should("have.value", expectedValue)
}
