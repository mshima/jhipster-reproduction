import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('Label e2e test', () => {
  const labelPageUrl = '/label';
  let username: string;
  let password: string;

  let label;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/labels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/labels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/labels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (label) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/labels/${label.id}`,
      }).then(() => {
        label = undefined;
      });
    }
  });

  it('Labels menu should load Labels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('label');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Label').should('exist');
    cy.location('pathname').should('eq', labelPageUrl);
  });

  describe('Label page', () => {
    it('should have translated page title', () => {
      cy.visit(labelPageUrl);
      cy.getEntityHeading('Label').should('not.contain', 'sampleWebfluxPsqlApp.testRootLabel.home.title');
    });

    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(labelPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Label page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('label');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', labelPageUrl);
      });
    });
  });
});
