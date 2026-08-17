import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('CustomPackageChild e2e test', () => {
  const customPackageChildPageUrl = '/custom-package-child';
  let username: string;
  let password: string;
  const customPackageChildSample = { childName: 'during hmph upliftingly' };

  let customPackageChild;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/custom-package-children+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/custom-package-children').as('postEntityRequest');
    cy.intercept('DELETE', '/api/custom-package-children/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customPackageChild) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/custom-package-children/${customPackageChild.id}`,
      }).then(() => {
        customPackageChild = undefined;
      });
    }
  });

  it('CustomPackageChildren menu should load CustomPackageChildren page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('custom-package-child');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomPackageChild').should('exist');
    cy.location('pathname').should('eq', customPackageChildPageUrl);
  });

  describe('CustomPackageChild page', () => {
    it('should have translated page title', () => {
      cy.visit(customPackageChildPageUrl);
      cy.getEntityHeading('CustomPackageChild').should('not.contain', 'jhipsterApp.customPackageChild.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customPackageChildPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomPackageChild page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${customPackageChildPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustomPackageChild');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageChildPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/custom-package-children',
          body: customPackageChildSample,
        }).then(({ body }) => {
          customPackageChild = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/custom-package-children+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customPackageChild],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(customPackageChildPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomPackageChild page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customPackageChild');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageChildPageUrl);
      });

      it('edit button click should load edit CustomPackageChild page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomPackageChild');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageChildPageUrl);
      });

      it('edit button click should load edit CustomPackageChild page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomPackageChild');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageChildPageUrl);
      });

      it('last delete button click should delete instance of CustomPackageChild', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customPackageChild').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageChildPageUrl);

        customPackageChild = undefined;
      });
    });
  });

  describe('new CustomPackageChild page', () => {
    beforeEach(() => {
      cy.visit(customPackageChildPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustomPackageChild');
    });

    it('should create an instance of CustomPackageChild', () => {
      cy.get(`[data-cy="childName"]`).type('hence');
      cy.get(`[data-cy="childName"]`).should('have.value', 'hence');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        customPackageChild = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', customPackageChildPageUrl);
    });
  });
});
