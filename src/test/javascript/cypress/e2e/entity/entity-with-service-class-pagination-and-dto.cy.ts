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

describe('EntityWithServiceClassPaginationAndDTO e2e test', () => {
  const entityWithServiceClassPaginationAndDTOPageUrl = '/entity-with-service-class-pagination-and-dto';
  let username: string;
  let password: string;
  const entityWithServiceClassPaginationAndDTOSample = { lena: 'undergo' };

  let entityWithServiceClassPaginationAndDTO;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entity-with-service-class-pagination-and-dtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entity-with-service-class-pagination-and-dtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entity-with-service-class-pagination-and-dtos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (entityWithServiceClassPaginationAndDTO) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entity-with-service-class-pagination-and-dtos/${entityWithServiceClassPaginationAndDTO.id}`,
      }).then(() => {
        entityWithServiceClassPaginationAndDTO = undefined;
      });
    }
  });

  it('EntityWithServiceClassPaginationAndDTOS menu should load EntityWithServiceClassPaginationAndDTOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entity-with-service-class-pagination-and-dto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntityWithServiceClassPaginationAndDTO').should('exist');
    cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
  });

  describe('EntityWithServiceClassPaginationAndDTO page', () => {
    it('should have translated page title', () => {
      cy.visit(entityWithServiceClassPaginationAndDTOPageUrl);
      cy.getEntityHeading('EntityWithServiceClassPaginationAndDTO').should(
        'not.contain',
        'sampleWebfluxPsqlApp.entityWithServiceClassPaginationAndDTO.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entityWithServiceClassPaginationAndDTOPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntityWithServiceClassPaginationAndDTO page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${entityWithServiceClassPaginationAndDTOPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassPaginationAndDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entity-with-service-class-pagination-and-dtos',
          body: entityWithServiceClassPaginationAndDTOSample,
        }).then(({ body }) => {
          entityWithServiceClassPaginationAndDTO = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entity-with-service-class-pagination-and-dtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/entity-with-service-class-pagination-and-dtos?page=0&size=20>; rel="last",<http://localhost/api/entity-with-service-class-pagination-and-dtos?page=0&size=20>; rel="first"',
              },
              body: [entityWithServiceClassPaginationAndDTO],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entityWithServiceClassPaginationAndDTOPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntityWithServiceClassPaginationAndDTO page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entityWithServiceClassPaginationAndDTO');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
      });

      it('edit button click should load edit EntityWithServiceClassPaginationAndDTO page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassPaginationAndDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
      });

      it('edit button click should load edit EntityWithServiceClassPaginationAndDTO page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceClassPaginationAndDTO');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
      });

      it('last delete button click should delete instance of EntityWithServiceClassPaginationAndDTO', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('entityWithServiceClassPaginationAndDTO').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);

        entityWithServiceClassPaginationAndDTO = undefined;
      });
    });
  });

  describe('new EntityWithServiceClassPaginationAndDTO page', () => {
    beforeEach(() => {
      cy.visit(entityWithServiceClassPaginationAndDTOPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntityWithServiceClassPaginationAndDTO');
    });

    it('should create an instance of EntityWithServiceClassPaginationAndDTO', () => {
      cy.get(`[data-cy="lena"]`).type('huzzah toward minus');
      cy.get(`[data-cy="lena"]`).should('have.value', 'huzzah toward minus');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entityWithServiceClassPaginationAndDTO = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', entityWithServiceClassPaginationAndDTOPageUrl);
    });
  });
});
