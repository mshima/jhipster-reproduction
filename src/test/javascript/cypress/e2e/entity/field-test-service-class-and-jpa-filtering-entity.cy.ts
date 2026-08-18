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

describe('FieldTestServiceClassAndJpaFilteringEntity e2e test', () => {
  const fieldTestServiceClassAndJpaFilteringEntityPageUrl = '/field-test-service-class-and-jpa-filtering-entity';
  let username: string;
  let password: string;
  const fieldTestServiceClassAndJpaFilteringEntitySample = {
    stringRequiredBob: 'during',
    integerRequiredBob: 12930,
    longRequiredBob: 23055,
    floatRequiredBob: 14755.71,
    doubleRequiredBob: 6097.72,
    bigDecimalRequiredBob: 4400.7,
    localDateRequiredBob: '2016-02-08',
    instanteRequiredBob: '2016-02-08T04:26:57.362Z',
    zonedDateTimeRequiredBob: '2016-02-08T11:39:24.962Z',
    localTimeRequiredBob: '18:28:00',
    durationRequiredBob: 15120,
    booleanRequiredBob: false,
    enumRequiredBob: 'ENUM_VALUE_2',
    uuidRequiredBob: '20348433-95d1-4007-ae35-1acccf1fed73',
    byteImageRequiredBob: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredBobContentType: 'unknown',
    byteAnyRequiredBob: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredBobContentType: 'unknown',
    byteTextRequiredBob: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestServiceClassAndJpaFilteringEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-service-class-and-jpa-filtering-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-service-class-and-jpa-filtering-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-service-class-and-jpa-filtering-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestServiceClassAndJpaFilteringEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-service-class-and-jpa-filtering-entities/${fieldTestServiceClassAndJpaFilteringEntity.id}`,
      }).then(() => {
        fieldTestServiceClassAndJpaFilteringEntity = undefined;
      });
    }
  });

  it('FieldTestServiceClassAndJpaFilteringEntities menu should load FieldTestServiceClassAndJpaFilteringEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-service-class-and-jpa-filtering-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestServiceClassAndJpaFilteringEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
  });

  describe('FieldTestServiceClassAndJpaFilteringEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      cy.getEntityHeading('FieldTestServiceClassAndJpaFilteringEntity').should(
        'not.contain',
        'sampleWebfluxPsqlApp.fieldTestServiceClassAndJpaFilteringEntity.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestServiceClassAndJpaFilteringEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestServiceClassAndJpaFilteringEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestServiceClassAndJpaFilteringEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestServiceClassAndJpaFilteringEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-service-class-and-jpa-filtering-entities',
          body: fieldTestServiceClassAndJpaFilteringEntitySample,
        }).then(({ body }) => {
          fieldTestServiceClassAndJpaFilteringEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-service-class-and-jpa-filtering-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fieldTestServiceClassAndJpaFilteringEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestServiceClassAndJpaFilteringEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestServiceClassAndJpaFilteringEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestServiceClassAndJpaFilteringEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      });

      it('edit button click should load edit FieldTestServiceClassAndJpaFilteringEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestServiceClassAndJpaFilteringEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      });

      it('edit button click should load edit FieldTestServiceClassAndJpaFilteringEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestServiceClassAndJpaFilteringEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestServiceClassAndJpaFilteringEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestServiceClassAndJpaFilteringEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);

        fieldTestServiceClassAndJpaFilteringEntity = undefined;
      });
    });
  });

  describe('new FieldTestServiceClassAndJpaFilteringEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestServiceClassAndJpaFilteringEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestServiceClassAndJpaFilteringEntity');
    });

    it('should create an instance of FieldTestServiceClassAndJpaFilteringEntity', () => {
      cy.get(`[data-cy="stringBob"]`).type('but fireplace yahoo');
      cy.get(`[data-cy="stringBob"]`).should('have.value', 'but fireplace yahoo');

      cy.get(`[data-cy="stringRequiredBob"]`).type('healthily next');
      cy.get(`[data-cy="stringRequiredBob"]`).should('have.value', 'healthily next');

      cy.get(`[data-cy="stringMinlengthBob"]`).type('utilized');
      cy.get(`[data-cy="stringMinlengthBob"]`).should('have.value', 'utilized');

      cy.get(`[data-cy="stringMaxlengthBob"]`).type('given');
      cy.get(`[data-cy="stringMaxlengthBob"]`).should('have.value', 'given');

      cy.get(`[data-cy="stringPatternBob"]`).type('1FV1');
      cy.get(`[data-cy="stringPatternBob"]`).should('have.value', '1FV1');

      cy.get(`[data-cy="integerBob"]`).type('31434');
      cy.get(`[data-cy="integerBob"]`).should('have.value', '31434');

      cy.get(`[data-cy="integerRequiredBob"]`).type('20756');
      cy.get(`[data-cy="integerRequiredBob"]`).should('have.value', '20756');

      cy.get(`[data-cy="integerMinBob"]`).type('16610');
      cy.get(`[data-cy="integerMinBob"]`).should('have.value', '16610');

      cy.get(`[data-cy="integerMaxBob"]`).type('14');
      cy.get(`[data-cy="integerMaxBob"]`).should('have.value', '14');

      cy.get(`[data-cy="longBob"]`).type('5214');
      cy.get(`[data-cy="longBob"]`).should('have.value', '5214');

      cy.get(`[data-cy="longRequiredBob"]`).type('18631');
      cy.get(`[data-cy="longRequiredBob"]`).should('have.value', '18631');

      cy.get(`[data-cy="longMinBob"]`).type('21031');
      cy.get(`[data-cy="longMinBob"]`).should('have.value', '21031');

      cy.get(`[data-cy="longMaxBob"]`).type('52');
      cy.get(`[data-cy="longMaxBob"]`).should('have.value', '52');

      cy.get(`[data-cy="floatBob"]`).type('31854.85');
      cy.get(`[data-cy="floatBob"]`).should('have.value', '31854.85');

      cy.get(`[data-cy="floatRequiredBob"]`).type('19844.42');
      cy.get(`[data-cy="floatRequiredBob"]`).should('have.value', '19844.42');

      cy.get(`[data-cy="floatMinBob"]`).type('2118.64');
      cy.get(`[data-cy="floatMinBob"]`).should('have.value', '2118.64');

      cy.get(`[data-cy="floatMaxBob"]`).type('40.36');
      cy.get(`[data-cy="floatMaxBob"]`).should('have.value', '40.36');

      cy.get(`[data-cy="doubleRequiredBob"]`).type('28645.25');
      cy.get(`[data-cy="doubleRequiredBob"]`).should('have.value', '28645.25');

      cy.get(`[data-cy="doubleMinBob"]`).type('16041.72');
      cy.get(`[data-cy="doubleMinBob"]`).should('have.value', '16041.72');

      cy.get(`[data-cy="doubleMaxBob"]`).type('30.42');
      cy.get(`[data-cy="doubleMaxBob"]`).should('have.value', '30.42');

      cy.get(`[data-cy="bigDecimalRequiredBob"]`).type('6608.98');
      cy.get(`[data-cy="bigDecimalRequiredBob"]`).should('have.value', '6608.98');

      cy.get(`[data-cy="bigDecimalMinBob"]`).type('14168.62');
      cy.get(`[data-cy="bigDecimalMinBob"]`).should('have.value', '14168.62');

      cy.get(`[data-cy="bigDecimalMaxBob"]`).type('49.97');
      cy.get(`[data-cy="bigDecimalMaxBob"]`).should('have.value', '49.97');

      cy.get(`[data-cy="localDateBob"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateBob"]`).blur();
      cy.get(`[data-cy="localDateBob"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredBob"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateRequiredBob"]`).blur();
      cy.get(`[data-cy="localDateRequiredBob"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="instantBob"]`).type('2016-02-08T10:21');
      cy.get(`[data-cy="instantBob"]`).blur();
      cy.get(`[data-cy="instantBob"]`).should('have.value', '2016-02-08T10:21');

      cy.get(`[data-cy="instanteRequiredBob"]`).type('2016-02-07T19:17');
      cy.get(`[data-cy="instanteRequiredBob"]`).blur();
      cy.get(`[data-cy="instanteRequiredBob"]`).should('have.value', '2016-02-07T19:17');

      cy.get(`[data-cy="zonedDateTimeBob"]`).type('2016-02-08T02:25');
      cy.get(`[data-cy="zonedDateTimeBob"]`).blur();
      cy.get(`[data-cy="zonedDateTimeBob"]`).should('have.value', '2016-02-08T02:25');

      cy.get(`[data-cy="zonedDateTimeRequiredBob"]`).type('2016-02-08T02:41');
      cy.get(`[data-cy="zonedDateTimeRequiredBob"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredBob"]`).should('have.value', '2016-02-08T02:41');

      cy.get(`[data-cy="localTimeBob"]`).type('00:35:00');
      cy.get(`[data-cy="localTimeBob"]`).invoke('val').should('match', new RegExp('00:35:00'));

      cy.get(`[data-cy="localTimeRequiredBob"]`).type('04:57:00');
      cy.get(`[data-cy="localTimeRequiredBob"]`).invoke('val').should('match', new RegExp('04:57:00'));

      cy.get(`[data-cy="durationBob"]`).type('PT11M');
      cy.get(`[data-cy="durationBob"]`).blur();
      cy.get(`[data-cy="durationBob"]`).should('have.value', 'PT11M');

      cy.get(`[data-cy="durationRequiredBob"]`).type('PT58M');
      cy.get(`[data-cy="durationRequiredBob"]`).blur();
      cy.get(`[data-cy="durationRequiredBob"]`).should('have.value', 'PT58M');

      cy.get(`[data-cy="booleanBob"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanBob"]`).click();
      cy.get(`[data-cy="booleanBob"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredBob"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredBob"]`).click();
      cy.get(`[data-cy="booleanRequiredBob"]`).should('be.checked');

      cy.get(`[data-cy="enumBob"]`).select('ENUM_VALUE_3');

      cy.get(`[data-cy="enumRequiredBob"]`).select('ENUM_VALUE_2');

      cy.get(`[data-cy="uuidBob"]`).type('dcd73738-bf97-48b9-ba29-480a0c229ecc');
      cy.get(`[data-cy="uuidBob"]`).invoke('val').should('match', new RegExp('dcd73738-bf97-48b9-ba29-480a0c229ecc'));

      cy.get(`[data-cy="uuidRequiredBob"]`).type('3e0ac41e-d4c3-47a3-9a43-5ac146a5a061');
      cy.get(`[data-cy="uuidRequiredBob"]`).invoke('val').should('match', new RegExp('3e0ac41e-d4c3-47a3-9a43-5ac146a5a061'));

      cy.setFieldImageAsBytesOfEntity('byteImageBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesBob', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesBob', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextBob"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextBob"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredBob"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredBob"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestServiceClassAndJpaFilteringEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestServiceClassAndJpaFilteringEntityPageUrl);
    });
  });
});
