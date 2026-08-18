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

describe('EntityWithServiceImplAndDTO e2e test', () => {
  const entityWithServiceImplAndDTOPageUrl = '/entity-with-service-impl-and-dto';
  let username: string;
  let password: string;
  const entityWithServiceImplAndDTOSample = { louis: 'surprisingly' };

  let entityWithServiceImplAndDTO;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entity-with-service-impl-and-dtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entity-with-service-impl-and-dtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entity-with-service-impl-and-dtos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (entityWithServiceImplAndDTO) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entity-with-service-impl-and-dtos/${entityWithServiceImplAndDTO.id}`,
      }).then(() => {
        entityWithServiceImplAndDTO = undefined;
      });
    }
  });

  it('EntityWithServiceImplAndDTOS menu should load EntityWithServiceImplAndDTOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entity-with-service-impl-and-dto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntityWithServiceImplAndDTO').should('exist');
    cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
  });

  describe('EntityWithServiceImplAndDTO page', () => {
    it('should have translated page title', () => {
      cy.visit(entityWithServiceImplAndDTOPageUrl);
      cy.getEntityHeading('EntityWithServiceImplAndDTO').should(
        'not.contain',
        'sampleWebfluxPsqlApp.entityWithServiceImplAndDTO.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entityWithServiceImplAndDTOPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntityWithServiceImplAndDTO page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${entityWithServiceImplAndDTOPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entity-with-service-impl-and-dtos',
          body: entityWithServiceImplAndDTOSample,
        }).then(({ body }) => {
          entityWithServiceImplAndDTO = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entity-with-service-impl-and-dtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [entityWithServiceImplAndDTO],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entityWithServiceImplAndDTOPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntityWithServiceImplAndDTO page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entityWithServiceImplAndDTO');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
      });

      it('edit button click should load edit EntityWithServiceImplAndDTO page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
      });

      it('edit button click should load edit EntityWithServiceImplAndDTO page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndDTO');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
      });

      it('last delete button click should delete instance of EntityWithServiceImplAndDTO', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('entityWithServiceImplAndDTO').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);

        entityWithServiceImplAndDTO = undefined;
      });
    });
  });

  describe('new EntityWithServiceImplAndDTO page', () => {
    beforeEach(() => {
      cy.visit(entityWithServiceImplAndDTOPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntityWithServiceImplAndDTO');
    });

    it('should create an instance of EntityWithServiceImplAndDTO', () => {
      cy.get(`[data-cy="louis"]`).type('lest farmer');
      cy.get(`[data-cy="louis"]`).should('have.value', 'lest farmer');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entityWithServiceImplAndDTO = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', entityWithServiceImplAndDTOPageUrl);
    });
  });
});
