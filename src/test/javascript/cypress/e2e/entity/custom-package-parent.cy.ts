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

describe('CustomPackageParent e2e test', () => {
  const customPackageParentPageUrl = '/custom-package-parent';
  let username: string;
  let password: string;
  const customPackageParentSample = { parentName: 'vivaciously' };

  let customPackageParent;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/custom-package-parents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/custom-package-parents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/custom-package-parents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customPackageParent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/custom-package-parents/${customPackageParent.id}`,
      }).then(() => {
        customPackageParent = undefined;
      });
    }
  });

  it('CustomPackageParents menu should load CustomPackageParents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('custom-package-parent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomPackageParent').should('exist');
    cy.location('pathname').should('eq', customPackageParentPageUrl);
  });

  describe('CustomPackageParent page', () => {
    it('should have translated page title', () => {
      cy.visit(customPackageParentPageUrl);
      cy.getEntityHeading('CustomPackageParent').should('not.contain', 'sampleWebfluxH2MemApp.customPackageParent.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customPackageParentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomPackageParent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${customPackageParentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustomPackageParent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageParentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/custom-package-parents',
          body: customPackageParentSample,
        }).then(({ body }) => {
          customPackageParent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/custom-package-parents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customPackageParent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(customPackageParentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomPackageParent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customPackageParent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageParentPageUrl);
      });

      it('edit button click should load edit CustomPackageParent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomPackageParent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageParentPageUrl);
      });

      it('edit button click should load edit CustomPackageParent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomPackageParent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageParentPageUrl);
      });

      it('last delete button click should delete instance of CustomPackageParent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customPackageParent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', customPackageParentPageUrl);

        customPackageParent = undefined;
      });
    });
  });

  describe('new CustomPackageParent page', () => {
    beforeEach(() => {
      cy.visit(customPackageParentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustomPackageParent');
    });

    it('should create an instance of CustomPackageParent', () => {
      cy.get(`[data-cy="parentName"]`).type('midst foolishly');
      cy.get(`[data-cy="parentName"]`).should('have.value', 'midst foolishly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        customPackageParent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', customPackageParentPageUrl);
    });
  });
});
