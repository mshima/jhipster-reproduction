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

describe('EntityWithDTO e2e test', () => {
  const entityWithDTOPageUrl = '/entity-with-dto';
  let username: string;
  let password: string;
  const entityWithDTOSample = { emma: 'kowtow now ack' };

  let entityWithDTO;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entity-with-dtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entity-with-dtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entity-with-dtos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (entityWithDTO) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entity-with-dtos/${entityWithDTO.id}`,
      }).then(() => {
        entityWithDTO = undefined;
      });
    }
  });

  it('EntityWithDTOS menu should load EntityWithDTOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entity-with-dto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntityWithDTO').should('exist');
    cy.location('pathname').should('eq', entityWithDTOPageUrl);
  });

  describe('EntityWithDTO page', () => {
    it('should have translated page title', () => {
      cy.visit(entityWithDTOPageUrl);
      cy.getEntityHeading('EntityWithDTO').should('not.contain', 'sampleWebfluxPsqlApp.entityWithDTO.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entityWithDTOPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntityWithDTO page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${entityWithDTOPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EntityWithDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithDTOPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entity-with-dtos',
          body: entityWithDTOSample,
        }).then(({ body }) => {
          entityWithDTO = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entity-with-dtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [entityWithDTO],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entityWithDTOPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntityWithDTO page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entityWithDTO');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithDTOPageUrl);
      });

      it('edit button click should load edit EntityWithDTO page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithDTOPageUrl);
      });

      it('edit button click should load edit EntityWithDTO page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntityWithDTO');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithDTOPageUrl);
      });

      it('last delete button click should delete instance of EntityWithDTO', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('entityWithDTO').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', entityWithDTOPageUrl);

        entityWithDTO = undefined;
      });
    });
  });

  describe('new EntityWithDTO page', () => {
    beforeEach(() => {
      cy.visit(entityWithDTOPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntityWithDTO');
    });

    it('should create an instance of EntityWithDTO', () => {
      cy.get(`[data-cy="emma"]`).type('scent');
      cy.get(`[data-cy="emma"]`).should('have.value', 'scent');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entityWithDTO = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', entityWithDTOPageUrl);
    });
  });
});
