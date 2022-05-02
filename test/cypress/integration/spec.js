it("Has correct title", () => {
	cy.visit("/");

	cy.get("title").should("contain", "Kancl.online")
});
