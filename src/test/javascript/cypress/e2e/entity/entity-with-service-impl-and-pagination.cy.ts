import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('EntityWithServiceImplAndPagination e2e test', () => {
  const entityWithServiceImplAndPaginationPageUrl = '/entity-with-service-impl-and-pagination';
  let username: string;
  let password: string;
  const entityWithServiceImplAndPaginationSample = { hugo: 'yippee hmph' };

  let entityWithServiceImplAndPagination;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entity-with-service-impl-and-paginations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entity-with-service-impl-and-paginations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entity-with-service-impl-and-paginations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (entityWithServiceImplAndPagination) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entity-with-service-impl-and-paginations/${entityWithServiceImplAndPagination.id}`,
      }).then(() => {
        entityWithServiceImplAndPagination = undefined;
      });
    }
  });

  it('EntityWithServiceImplAndPaginations menu should load EntityWithServiceImplAndPaginations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entity-with-service-impl-and-pagination');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntityWithServiceImplAndPagination').should('exist');
    cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
  });

  describe('EntityWithServiceImplAndPagination page', () => {
    it('should have translated page title', () => {
      cy.visit(entityWithServiceImplAndPaginationPageUrl);
      cy.getEntityHeading('EntityWithServiceImplAndPagination').should(
        'not.contain',
        'sampleWebfluxPsqlApp.entityWithServiceImplAndPagination.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entityWithServiceImplAndPaginationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntityWithServiceImplAndPagination page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${entityWithServiceImplAndPaginationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndPagination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entity-with-service-impl-and-paginations',
          body: entityWithServiceImplAndPaginationSample,
        }).then(({ body }) => {
          entityWithServiceImplAndPagination = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entity-with-service-impl-and-paginations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/entity-with-service-impl-and-paginations?page=0&size=20>; rel="last",<http://localhost/api/entity-with-service-impl-and-paginations?page=0&size=20>; rel="first"',
              },
              body: [entityWithServiceImplAndPagination],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entityWithServiceImplAndPaginationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntityWithServiceImplAndPagination page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entityWithServiceImplAndPagination');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
      });

      it('edit button click should load edit EntityWithServiceImplAndPagination page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndPagination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
      });

      it('edit button click should load edit EntityWithServiceImplAndPagination page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndPagination');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
      });

      it('last delete button click should delete instance of EntityWithServiceImplAndPagination', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('entityWithServiceImplAndPagination').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);

        entityWithServiceImplAndPagination = undefined;
      });
    });
  });

  describe('new EntityWithServiceImplAndPagination page', () => {
    beforeEach(() => {
      cy.visit(entityWithServiceImplAndPaginationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndPagination');
    });

    it('should create an instance of EntityWithServiceImplAndPagination', () => {
      cy.get(`[data-cy="hugo"]`).type('opposite ruin championship');
      cy.get(`[data-cy="hugo"]`).should('have.value', 'opposite ruin championship');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entityWithServiceImplAndPagination = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', entityWithServiceImplAndPaginationPageUrl);
    });
  });
});
