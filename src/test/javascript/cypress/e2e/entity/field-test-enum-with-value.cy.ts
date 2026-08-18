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

describe('FieldTestEnumWithValue e2e test', () => {
  const fieldTestEnumWithValuePageUrl = '/field-test-enum-with-value';
  let username: string;
  let password: string;
  const fieldTestEnumWithValueSample = { myFieldA: 'AAA' };

  let fieldTestEnumWithValue;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-enum-with-values+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-enum-with-values').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-enum-with-values/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestEnumWithValue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-enum-with-values/${fieldTestEnumWithValue.id}`,
      }).then(() => {
        fieldTestEnumWithValue = undefined;
      });
    }
  });

  it('FieldTestEnumWithValues menu should load FieldTestEnumWithValues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-enum-with-value');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestEnumWithValue').should('exist');
    cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
  });

  describe('FieldTestEnumWithValue page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestEnumWithValuePageUrl);
      cy.getEntityHeading('FieldTestEnumWithValue').should('not.contain', 'sampleWebfluxPsqlApp.fieldTestEnumWithValue.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestEnumWithValuePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestEnumWithValue page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestEnumWithValuePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestEnumWithValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-enum-with-values',
          body: fieldTestEnumWithValueSample,
        }).then(({ body }) => {
          fieldTestEnumWithValue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-enum-with-values+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fieldTestEnumWithValue],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestEnumWithValuePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestEnumWithValue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestEnumWithValue');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
      });

      it('edit button click should load edit FieldTestEnumWithValue page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestEnumWithValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
      });

      it('edit button click should load edit FieldTestEnumWithValue page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestEnumWithValue');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
      });

      it('last delete button click should delete instance of FieldTestEnumWithValue', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestEnumWithValue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);

        fieldTestEnumWithValue = undefined;
      });
    });
  });

  describe('new FieldTestEnumWithValue page', () => {
    beforeEach(() => {
      cy.visit(fieldTestEnumWithValuePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestEnumWithValue');
    });

    it('should create an instance of FieldTestEnumWithValue', () => {
      cy.get(`[data-cy="myFieldA"]`).select('AAA');

      cy.get(`[data-cy="myFieldB"]`).select('BBB');

      cy.get(`[data-cy="myFieldC"]`).select('AAA');

      cy.get(`[data-cy="myFieldD"]`).select('BBB');

      cy.get(`[data-cy="myFieldE"]`).select('BBB');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestEnumWithValue = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestEnumWithValuePageUrl);
    });
  });
});
