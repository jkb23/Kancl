export function openMainPage() {
	cy.visit("/");
}

export function showsConnectedUser(name) {
	cy.get("p").should("contain", name);
}

export function showsEmptyMeeting()
{
	cy.get("p").should("contain", "No participant")
}
