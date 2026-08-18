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

describe('FieldTestMapstructAndServiceClassEntity e2e test', () => {
  const fieldTestMapstructAndServiceClassEntityPageUrl = '/field-test-mapstruct-and-service-class-entity';
  let username: string;
  let password: string;
  const fieldTestMapstructAndServiceClassEntitySample = {
    stringRequiredEva: 'across aha',
    integerRequiredEva: 12144,
    longRequiredEva: 11110,
    floatRequiredEva: 6515.24,
    doubleRequiredEva: 32310.02,
    bigDecimalRequiredEva: 31024.4,
    localDateRequiredEva: '2016-02-07',
    instanteRequiredEva: '2016-02-08T03:17:57.933Z',
    zonedDateTimeRequiredEva: '2016-02-07T19:55:46.138Z',
    localTimeRequiredEva: '04:41:00',
    durationRequiredEva: 559,
    booleanRequiredEva: false,
    enumRequiredEva: 'ENUM_VALUE_1',
    uuidRequiredEva: 'baee604e-039b-46a0-be52-d49bd08106e6',
    byteImageRequiredEva: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredEvaContentType: 'unknown',
    byteAnyRequiredEva: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredEvaContentType: 'unknown',
    byteTextRequiredEva: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestMapstructAndServiceClassEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-mapstruct-and-service-class-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-mapstruct-and-service-class-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-mapstruct-and-service-class-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestMapstructAndServiceClassEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-mapstruct-and-service-class-entities/${fieldTestMapstructAndServiceClassEntity.id}`,
      }).then(() => {
        fieldTestMapstructAndServiceClassEntity = undefined;
      });
    }
  });

  it('FieldTestMapstructAndServiceClassEntities menu should load FieldTestMapstructAndServiceClassEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-mapstruct-and-service-class-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestMapstructAndServiceClassEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
  });

  describe('FieldTestMapstructAndServiceClassEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestMapstructAndServiceClassEntityPageUrl);
      cy.getEntityHeading('FieldTestMapstructAndServiceClassEntity').should(
        'not.contain',
        'sampleWebfluxPsqlApp.fieldTestMapstructAndServiceClassEntity.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestMapstructAndServiceClassEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestMapstructAndServiceClassEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestMapstructAndServiceClassEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestMapstructAndServiceClassEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-mapstruct-and-service-class-entities',
          body: fieldTestMapstructAndServiceClassEntitySample,
        }).then(({ body }) => {
          fieldTestMapstructAndServiceClassEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-mapstruct-and-service-class-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fieldTestMapstructAndServiceClassEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestMapstructAndServiceClassEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestMapstructAndServiceClassEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestMapstructAndServiceClassEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
      });

      it('edit button click should load edit FieldTestMapstructAndServiceClassEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestMapstructAndServiceClassEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
      });

      it('edit button click should load edit FieldTestMapstructAndServiceClassEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestMapstructAndServiceClassEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestMapstructAndServiceClassEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestMapstructAndServiceClassEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);

        fieldTestMapstructAndServiceClassEntity = undefined;
      });
    });
  });

  describe('new FieldTestMapstructAndServiceClassEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestMapstructAndServiceClassEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestMapstructAndServiceClassEntity');
    });

    it('should create an instance of FieldTestMapstructAndServiceClassEntity', () => {
      cy.get(`[data-cy="stringEva"]`).type('anenst quiet');
      cy.get(`[data-cy="stringEva"]`).should('have.value', 'anenst quiet');

      cy.get(`[data-cy="stringRequiredEva"]`).type('generally');
      cy.get(`[data-cy="stringRequiredEva"]`).should('have.value', 'generally');

      cy.get(`[data-cy="stringMinlengthEva"]`).type('athwart treble elegantly');
      cy.get(`[data-cy="stringMinlengthEva"]`).should('have.value', 'athwart treble elegantly');

      cy.get(`[data-cy="stringMaxlengthEva"]`).type('since self-reliant');
      cy.get(`[data-cy="stringMaxlengthEva"]`).should('have.value', 'since self-reliant');

      cy.get(`[data-cy="stringPatternEva"]`).type('Z');
      cy.get(`[data-cy="stringPatternEva"]`).should('have.value', 'Z');

      cy.get(`[data-cy="integerEva"]`).type('3985');
      cy.get(`[data-cy="integerEva"]`).should('have.value', '3985');

      cy.get(`[data-cy="integerRequiredEva"]`).type('18240');
      cy.get(`[data-cy="integerRequiredEva"]`).should('have.value', '18240');

      cy.get(`[data-cy="integerMinEva"]`).type('7099');
      cy.get(`[data-cy="integerMinEva"]`).should('have.value', '7099');

      cy.get(`[data-cy="integerMaxEva"]`).type('44');
      cy.get(`[data-cy="integerMaxEva"]`).should('have.value', '44');

      cy.get(`[data-cy="longEva"]`).type('17202');
      cy.get(`[data-cy="longEva"]`).should('have.value', '17202');

      cy.get(`[data-cy="longRequiredEva"]`).type('2116');
      cy.get(`[data-cy="longRequiredEva"]`).should('have.value', '2116');

      cy.get(`[data-cy="longMinEva"]`).type('21937');
      cy.get(`[data-cy="longMinEva"]`).should('have.value', '21937');

      cy.get(`[data-cy="longMaxEva"]`).type('19');
      cy.get(`[data-cy="longMaxEva"]`).should('have.value', '19');

      cy.get(`[data-cy="floatEva"]`).type('10931.65');
      cy.get(`[data-cy="floatEva"]`).should('have.value', '10931.65');

      cy.get(`[data-cy="floatRequiredEva"]`).type('4333.48');
      cy.get(`[data-cy="floatRequiredEva"]`).should('have.value', '4333.48');

      cy.get(`[data-cy="floatMinEva"]`).type('28049.11');
      cy.get(`[data-cy="floatMinEva"]`).should('have.value', '28049.11');

      cy.get(`[data-cy="floatMaxEva"]`).type('84.01');
      cy.get(`[data-cy="floatMaxEva"]`).should('have.value', '84.01');

      cy.get(`[data-cy="doubleRequiredEva"]`).type('24467.45');
      cy.get(`[data-cy="doubleRequiredEva"]`).should('have.value', '24467.45');

      cy.get(`[data-cy="doubleMinEva"]`).type('20493.49');
      cy.get(`[data-cy="doubleMinEva"]`).should('have.value', '20493.49');

      cy.get(`[data-cy="doubleMaxEva"]`).type('22.64');
      cy.get(`[data-cy="doubleMaxEva"]`).should('have.value', '22.64');

      cy.get(`[data-cy="bigDecimalRequiredEva"]`).type('21862.05');
      cy.get(`[data-cy="bigDecimalRequiredEva"]`).should('have.value', '21862.05');

      cy.get(`[data-cy="bigDecimalMinEva"]`).type('7355.22');
      cy.get(`[data-cy="bigDecimalMinEva"]`).should('have.value', '7355.22');

      cy.get(`[data-cy="bigDecimalMaxEva"]`).type('84.51');
      cy.get(`[data-cy="bigDecimalMaxEva"]`).should('have.value', '84.51');

      cy.get(`[data-cy="localDateEva"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateEva"]`).blur();
      cy.get(`[data-cy="localDateEva"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredEva"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateRequiredEva"]`).blur();
      cy.get(`[data-cy="localDateRequiredEva"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="instantEva"]`).type('2016-02-08T14:15');
      cy.get(`[data-cy="instantEva"]`).blur();
      cy.get(`[data-cy="instantEva"]`).should('have.value', '2016-02-08T14:15');

      cy.get(`[data-cy="instanteRequiredEva"]`).type('2016-02-08T16:18');
      cy.get(`[data-cy="instanteRequiredEva"]`).blur();
      cy.get(`[data-cy="instanteRequiredEva"]`).should('have.value', '2016-02-08T16:18');

      cy.get(`[data-cy="zonedDateTimeEva"]`).type('2016-02-08T18:39');
      cy.get(`[data-cy="zonedDateTimeEva"]`).blur();
      cy.get(`[data-cy="zonedDateTimeEva"]`).should('have.value', '2016-02-08T18:39');

      cy.get(`[data-cy="zonedDateTimeRequiredEva"]`).type('2016-02-08T06:34');
      cy.get(`[data-cy="zonedDateTimeRequiredEva"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredEva"]`).should('have.value', '2016-02-08T06:34');

      cy.get(`[data-cy="localTimeEva"]`).type('15:55:00');
      cy.get(`[data-cy="localTimeEva"]`).invoke('val').should('match', new RegExp('15:55:00'));

      cy.get(`[data-cy="localTimeRequiredEva"]`).type('22:22:00');
      cy.get(`[data-cy="localTimeRequiredEva"]`).invoke('val').should('match', new RegExp('22:22:00'));

      cy.get(`[data-cy="durationEva"]`).type('PT48M');
      cy.get(`[data-cy="durationEva"]`).blur();
      cy.get(`[data-cy="durationEva"]`).should('have.value', 'PT48M');

      cy.get(`[data-cy="durationRequiredEva"]`).type('PT47M');
      cy.get(`[data-cy="durationRequiredEva"]`).blur();
      cy.get(`[data-cy="durationRequiredEva"]`).should('have.value', 'PT47M');

      cy.get(`[data-cy="booleanEva"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanEva"]`).click();
      cy.get(`[data-cy="booleanEva"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredEva"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredEva"]`).click();
      cy.get(`[data-cy="booleanRequiredEva"]`).should('be.checked');

      cy.get(`[data-cy="enumEva"]`).select('ENUM_VALUE_1');

      cy.get(`[data-cy="enumRequiredEva"]`).select('ENUM_VALUE_1');

      cy.get(`[data-cy="uuidEva"]`).type('b5a32474-cf1a-46b1-946e-44c64ad8fe0b');
      cy.get(`[data-cy="uuidEva"]`).invoke('val').should('match', new RegExp('b5a32474-cf1a-46b1-946e-44c64ad8fe0b'));

      cy.get(`[data-cy="uuidRequiredEva"]`).type('b066c1ae-4e7b-434c-b69f-8f0be3ad4b87');
      cy.get(`[data-cy="uuidRequiredEva"]`).invoke('val').should('match', new RegExp('b066c1ae-4e7b-434c-b69f-8f0be3ad4b87'));

      cy.setFieldImageAsBytesOfEntity('byteImageEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesEva', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesEva', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextEva"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextEva"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredEva"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredEva"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestMapstructAndServiceClassEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestMapstructAndServiceClassEntityPageUrl);
    });
  });
});
