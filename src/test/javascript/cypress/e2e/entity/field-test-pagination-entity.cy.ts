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

describe('FieldTestPaginationEntity e2e test', () => {
  const fieldTestPaginationEntityPageUrl = '/field-test-pagination-entity';
  let username: string;
  let password: string;
  const fieldTestPaginationEntitySample = {
    stringRequiredAlice: 'slope hastily',
    integerRequiredAlice: 14683,
    longRequiredAlice: 31929,
    floatRequiredAlice: 7302.04,
    doubleRequiredAlice: 28537.66,
    bigDecimalRequiredAlice: 861.75,
    localDateRequiredAlice: '2016-02-08',
    instanteRequiredAlice: '2016-02-07T20:39:41.669Z',
    zonedDateTimeRequiredAlice: '2016-02-08T13:00:43.651Z',
    localTimeRequiredAlice: '02:57:00',
    durationRequiredAlice: 7371,
    booleanRequiredAlice: true,
    enumRequiredAlice: 'ENUM_VALUE_2',
    uuidRequiredAlice: '9bf41ac4-2c4d-4bf5-8c10-a04c616dc81c',
    byteImageRequiredAlice: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredAliceContentType: 'unknown',
    byteAnyRequiredAlice: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredAliceContentType: 'unknown',
    byteTextRequiredAlice: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestPaginationEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-pagination-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-pagination-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-pagination-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestPaginationEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-pagination-entities/${fieldTestPaginationEntity.id}`,
      }).then(() => {
        fieldTestPaginationEntity = undefined;
      });
    }
  });

  it('FieldTestPaginationEntities menu should load FieldTestPaginationEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-pagination-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestPaginationEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
  });

  describe('FieldTestPaginationEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestPaginationEntityPageUrl);
      cy.getEntityHeading('FieldTestPaginationEntity').should('not.contain', 'sampleWebfluxPsqlApp.fieldTestPaginationEntity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestPaginationEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestPaginationEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestPaginationEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestPaginationEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-pagination-entities',
          body: fieldTestPaginationEntitySample,
        }).then(({ body }) => {
          fieldTestPaginationEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-pagination-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/field-test-pagination-entities?page=0&size=20>; rel="last",<http://localhost/api/field-test-pagination-entities?page=0&size=20>; rel="first"',
              },
              body: [fieldTestPaginationEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestPaginationEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestPaginationEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestPaginationEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
      });

      it('edit button click should load edit FieldTestPaginationEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestPaginationEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
      });

      it('edit button click should load edit FieldTestPaginationEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestPaginationEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestPaginationEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestPaginationEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);

        fieldTestPaginationEntity = undefined;
      });
    });
  });

  describe('new FieldTestPaginationEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestPaginationEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestPaginationEntity');
    });

    it('should create an instance of FieldTestPaginationEntity', () => {
      cy.get(`[data-cy="stringAlice"]`).type('supposing openly');
      cy.get(`[data-cy="stringAlice"]`).should('have.value', 'supposing openly');

      cy.get(`[data-cy="stringRequiredAlice"]`).type('hyena');
      cy.get(`[data-cy="stringRequiredAlice"]`).should('have.value', 'hyena');

      cy.get(`[data-cy="stringMinlengthAlice"]`).type('colorful');
      cy.get(`[data-cy="stringMinlengthAlice"]`).should('have.value', 'colorful');

      cy.get(`[data-cy="stringMaxlengthAlice"]`).type('silent book');
      cy.get(`[data-cy="stringMaxlengthAlice"]`).should('have.value', 'silent book');

      cy.get(`[data-cy="integerAlice"]`).type('12124');
      cy.get(`[data-cy="integerAlice"]`).should('have.value', '12124');

      cy.get(`[data-cy="integerRequiredAlice"]`).type('28799');
      cy.get(`[data-cy="integerRequiredAlice"]`).should('have.value', '28799');

      cy.get(`[data-cy="integerMinAlice"]`).type('13866');
      cy.get(`[data-cy="integerMinAlice"]`).should('have.value', '13866');

      cy.get(`[data-cy="integerMaxAlice"]`).type('18');
      cy.get(`[data-cy="integerMaxAlice"]`).should('have.value', '18');

      cy.get(`[data-cy="longAlice"]`).type('27955');
      cy.get(`[data-cy="longAlice"]`).should('have.value', '27955');

      cy.get(`[data-cy="longRequiredAlice"]`).type('20536');
      cy.get(`[data-cy="longRequiredAlice"]`).should('have.value', '20536');

      cy.get(`[data-cy="longMinAlice"]`).type('25242');
      cy.get(`[data-cy="longMinAlice"]`).should('have.value', '25242');

      cy.get(`[data-cy="longMaxAlice"]`).type('18');
      cy.get(`[data-cy="longMaxAlice"]`).should('have.value', '18');

      cy.get(`[data-cy="floatAlice"]`).type('9688.7');
      cy.get(`[data-cy="floatAlice"]`).should('have.value', '9688.7');

      cy.get(`[data-cy="floatRequiredAlice"]`).type('28522.95');
      cy.get(`[data-cy="floatRequiredAlice"]`).should('have.value', '28522.95');

      cy.get(`[data-cy="floatMinAlice"]`).type('27598.82');
      cy.get(`[data-cy="floatMinAlice"]`).should('have.value', '27598.82');

      cy.get(`[data-cy="floatMaxAlice"]`).type('77.74');
      cy.get(`[data-cy="floatMaxAlice"]`).should('have.value', '77.74');

      cy.get(`[data-cy="doubleRequiredAlice"]`).type('29484.46');
      cy.get(`[data-cy="doubleRequiredAlice"]`).should('have.value', '29484.46');

      cy.get(`[data-cy="doubleMinAlice"]`).type('27663.74');
      cy.get(`[data-cy="doubleMinAlice"]`).should('have.value', '27663.74');

      cy.get(`[data-cy="doubleMaxAlice"]`).type('55.67');
      cy.get(`[data-cy="doubleMaxAlice"]`).should('have.value', '55.67');

      cy.get(`[data-cy="bigDecimalRequiredAlice"]`).type('9081.84');
      cy.get(`[data-cy="bigDecimalRequiredAlice"]`).should('have.value', '9081.84');

      cy.get(`[data-cy="bigDecimalMinAlice"]`).type('21168.55');
      cy.get(`[data-cy="bigDecimalMinAlice"]`).should('have.value', '21168.55');

      cy.get(`[data-cy="bigDecimalMaxAlice"]`).type('19.95');
      cy.get(`[data-cy="bigDecimalMaxAlice"]`).should('have.value', '19.95');

      cy.get(`[data-cy="localDateAlice"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateAlice"]`).blur();
      cy.get(`[data-cy="localDateAlice"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredAlice"]`).type('2016-02-07');
      cy.get(`[data-cy="localDateRequiredAlice"]`).blur();
      cy.get(`[data-cy="localDateRequiredAlice"]`).should('have.value', '2016-02-07');

      cy.get(`[data-cy="instantAlice"]`).type('2016-02-07T22:08');
      cy.get(`[data-cy="instantAlice"]`).blur();
      cy.get(`[data-cy="instantAlice"]`).should('have.value', '2016-02-07T22:08');

      cy.get(`[data-cy="instanteRequiredAlice"]`).type('2016-02-08T10:45');
      cy.get(`[data-cy="instanteRequiredAlice"]`).blur();
      cy.get(`[data-cy="instanteRequiredAlice"]`).should('have.value', '2016-02-08T10:45');

      cy.get(`[data-cy="zonedDateTimeAlice"]`).type('2016-02-07T23:36');
      cy.get(`[data-cy="zonedDateTimeAlice"]`).blur();
      cy.get(`[data-cy="zonedDateTimeAlice"]`).should('have.value', '2016-02-07T23:36');

      cy.get(`[data-cy="zonedDateTimeRequiredAlice"]`).type('2016-02-08T09:27');
      cy.get(`[data-cy="zonedDateTimeRequiredAlice"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredAlice"]`).should('have.value', '2016-02-08T09:27');

      cy.get(`[data-cy="localTimeAlice"]`).type('02:07:00');
      cy.get(`[data-cy="localTimeAlice"]`).invoke('val').should('match', new RegExp('02:07:00'));

      cy.get(`[data-cy="localTimeRequiredAlice"]`).type('20:57:00');
      cy.get(`[data-cy="localTimeRequiredAlice"]`).invoke('val').should('match', new RegExp('20:57:00'));

      cy.get(`[data-cy="durationAlice"]`).type('PT54M');
      cy.get(`[data-cy="durationAlice"]`).blur();
      cy.get(`[data-cy="durationAlice"]`).should('have.value', 'PT54M');

      cy.get(`[data-cy="durationRequiredAlice"]`).type('PT48M');
      cy.get(`[data-cy="durationRequiredAlice"]`).blur();
      cy.get(`[data-cy="durationRequiredAlice"]`).should('have.value', 'PT48M');

      cy.get(`[data-cy="booleanAlice"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanAlice"]`).click();
      cy.get(`[data-cy="booleanAlice"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredAlice"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredAlice"]`).click();
      cy.get(`[data-cy="booleanRequiredAlice"]`).should('be.checked');

      cy.get(`[data-cy="enumAlice"]`).select('ENUM_VALUE_3');

      cy.get(`[data-cy="enumRequiredAlice"]`).select('ENUM_VALUE_1');

      cy.get(`[data-cy="uuidAlice"]`).type('e4f314b4-4db6-4ee6-ba38-0e13a80b1081');
      cy.get(`[data-cy="uuidAlice"]`).invoke('val').should('match', new RegExp('e4f314b4-4db6-4ee6-ba38-0e13a80b1081'));

      cy.get(`[data-cy="uuidRequiredAlice"]`).type('58390c29-3710-4393-9bb7-4c55ce974e88');
      cy.get(`[data-cy="uuidRequiredAlice"]`).invoke('val').should('match', new RegExp('58390c29-3710-4393-9bb7-4c55ce974e88'));

      cy.setFieldImageAsBytesOfEntity('byteImageAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesAlice', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesAlice', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextAlice"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextAlice"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredAlice"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredAlice"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestPaginationEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestPaginationEntityPageUrl);
    });
  });
});
