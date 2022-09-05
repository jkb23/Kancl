import * as application from '../testrig/application';
import * as zoom from '../testrig/zoom';

describe('Hello page', () => {
    it("says hello", () => {
        application.openHelloPage();
        application.showsHello();
    });
});
