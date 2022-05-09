export function openMainPage() {
	cy.visit("/");
}

export function showsConnectedUser(name) {
	cy.get("pre").should("contain", name);
}

export function showsEmptyMeeting()
{
	cy.get("pre").should("contain", "[]")
}
