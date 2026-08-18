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

describe('EntityWithServiceClassAndPagination e2e test', () => {
  const entityWithServiceClassAndPaginationPageUrl = '/entity-with-service-class-and-pagination';
  let username: string;
  let password: string;
  const entityWithServiceClassAndPaginationSample = { enzo: 'consequently publicity hungrily' };

  let entityWithServiceClassAndPagination;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entity-with-service-class-and-paginations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entity-with-service-class-and-paginations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entity-with-service-class-and-paginations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (entityWithServiceClassAndPagination) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entity-with-service-class-and-paginations/${entityWithServiceClassAndPagination.id}`,
      }).then(() => {
        entityWithServiceClassAndPagination = undefined;
      });
    }
  });

  it('EntityWithServiceClassAndPaginations menu should load EntityWithServiceClassAndPaginations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entity-with-service-class-and-pagination');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntityWithServiceClassAndPagination').should('exist');
    cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
  });

  describe('EntityWithServiceClassAndPagination page', () => {
    it('should have translated page title', () => {
      cy.visit(entityWithServiceClassAndPaginationPageUrl);
      cy.getEntityHeading('EntityWithServiceClassAndPagination').should(
        'not.contain',
        'sampleWebfluxPsqlApp.entityWithServiceClassAndPagination.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entityWithServiceClassAndPaginationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntityWithServiceClassAndPagination page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${entityWithServiceClassAndPaginationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassAndPagination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entity-with-service-class-and-paginations',
          body: entityWithServiceClassAndPaginationSample,
        }).then(({ body }) => {
          entityWithServiceClassAndPagination = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entity-with-service-class-and-paginations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/entity-with-service-class-and-paginations?page=0&size=20>; rel="last",<http://localhost/api/entity-with-service-class-and-paginations?page=0&size=20>; rel="first"',
              },
              body: [entityWithServiceClassAndPagination],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entityWithServiceClassAndPaginationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntityWithServiceClassAndPagination page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entityWithServiceClassAndPagination');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
      });

      it('edit button click should load edit EntityWithServiceClassAndPagination page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassAndPagination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
      });

      it('edit button click should load edit EntityWithServiceClassAndPagination page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassAndPagination');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
      });

      it('last delete button click should delete instance of EntityWithServiceClassAndPagination', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('entityWithServiceClassAndPagination').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);

        entityWithServiceClassAndPagination = undefined;
      });
    });
  });

  describe('new EntityWithServiceClassAndPagination page', () => {
    beforeEach(() => {
      cy.visit(entityWithServiceClassAndPaginationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntityWithServiceClassAndPagination');
    });

    it('should create an instance of EntityWithServiceClassAndPagination', () => {
      cy.get(`[data-cy="enzo"]`).type('out');
      cy.get(`[data-cy="enzo"]`).should('have.value', 'out');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entityWithServiceClassAndPagination = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', entityWithServiceClassAndPaginationPageUrl);
    });
  });
});
