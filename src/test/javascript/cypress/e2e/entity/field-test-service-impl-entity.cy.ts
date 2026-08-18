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

describe('FieldTestServiceImplEntity e2e test', () => {
  const fieldTestServiceImplEntityPageUrl = '/field-test-service-impl-entity';
  let username: string;
  let password: string;
  const fieldTestServiceImplEntitySample = {
    stringRequiredMika: 'shoulder who',
    integerRequiredMika: 23318,
    longRequiredMika: 6527,
    floatRequiredMika: 1797.33,
    doubleRequiredMika: 32466.36,
    bigDecimalRequiredMika: 15981.83,
    localDateRequiredMika: '2016-02-08',
    instanteRequiredMika: '2016-02-07T23:03:01.425Z',
    zonedDateTimeRequiredMika: '2016-02-08T12:05:00.402Z',
    localTimeRequiredMika: '21:08:00',
    durationRequiredMika: 8036,
    booleanRequiredMika: true,
    enumRequiredMika: 'ENUM_VALUE_1',
    uuidRequiredMika: '30c067da-adf6-49fd-94c8-5600a458c421',
    byteImageRequiredMika: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredMikaContentType: 'unknown',
    byteAnyRequiredMika: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredMikaContentType: 'unknown',
    byteTextRequiredMika: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestServiceImplEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-service-impl-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-service-impl-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-service-impl-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestServiceImplEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-service-impl-entities/${fieldTestServiceImplEntity.id}`,
      }).then(() => {
        fieldTestServiceImplEntity = undefined;
      });
    }
  });

  it('FieldTestServiceImplEntities menu should load FieldTestServiceImplEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-service-impl-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestServiceImplEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
  });

  describe('FieldTestServiceImplEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestServiceImplEntityPageUrl);
      cy.getEntityHeading('FieldTestServiceImplEntity').should('not.contain', 'sampleWebfluxPsqlApp.fieldTestServiceImplEntity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestServiceImplEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestServiceImplEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestServiceImplEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestServiceImplEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-service-impl-entities',
          body: fieldTestServiceImplEntitySample,
        }).then(({ body }) => {
          fieldTestServiceImplEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-service-impl-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fieldTestServiceImplEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestServiceImplEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestServiceImplEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestServiceImplEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
      });

      it('edit button click should load edit FieldTestServiceImplEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestServiceImplEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
      });

      it('edit button click should load edit FieldTestServiceImplEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestServiceImplEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestServiceImplEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestServiceImplEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);

        fieldTestServiceImplEntity = undefined;
      });
    });
  });

  describe('new FieldTestServiceImplEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestServiceImplEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestServiceImplEntity');
    });

    it('should create an instance of FieldTestServiceImplEntity', () => {
      cy.get(`[data-cy="stringMika"]`).type('fondly ick train');
      cy.get(`[data-cy="stringMika"]`).should('have.value', 'fondly ick train');

      cy.get(`[data-cy="stringRequiredMika"]`).type('lowball psst');
      cy.get(`[data-cy="stringRequiredMika"]`).should('have.value', 'lowball psst');

      cy.get(`[data-cy="stringMinlengthMika"]`).type('ugh');
      cy.get(`[data-cy="stringMinlengthMika"]`).should('have.value', 'ugh');

      cy.get(`[data-cy="stringMaxlengthMika"]`).type('yahoo huzzah');
      cy.get(`[data-cy="stringMaxlengthMika"]`).should('have.value', 'yahoo huzzah');

      cy.get(`[data-cy="stringPatternMika"]`).type('2KgIl');
      cy.get(`[data-cy="stringPatternMika"]`).should('have.value', '2KgIl');

      cy.get(`[data-cy="integerMika"]`).type('15439');
      cy.get(`[data-cy="integerMika"]`).should('have.value', '15439');

      cy.get(`[data-cy="integerRequiredMika"]`).type('29684');
      cy.get(`[data-cy="integerRequiredMika"]`).should('have.value', '29684');

      cy.get(`[data-cy="integerMinMika"]`).type('21487');
      cy.get(`[data-cy="integerMinMika"]`).should('have.value', '21487');

      cy.get(`[data-cy="integerMaxMika"]`).type('57');
      cy.get(`[data-cy="integerMaxMika"]`).should('have.value', '57');

      cy.get(`[data-cy="longMika"]`).type('28101');
      cy.get(`[data-cy="longMika"]`).should('have.value', '28101');

      cy.get(`[data-cy="longRequiredMika"]`).type('13668');
      cy.get(`[data-cy="longRequiredMika"]`).should('have.value', '13668');

      cy.get(`[data-cy="longMinMika"]`).type('4787');
      cy.get(`[data-cy="longMinMika"]`).should('have.value', '4787');

      cy.get(`[data-cy="longMaxMika"]`).type('82');
      cy.get(`[data-cy="longMaxMika"]`).should('have.value', '82');

      cy.get(`[data-cy="floatMika"]`).type('21370.96');
      cy.get(`[data-cy="floatMika"]`).should('have.value', '21370.96');

      cy.get(`[data-cy="floatRequiredMika"]`).type('1814.71');
      cy.get(`[data-cy="floatRequiredMika"]`).should('have.value', '1814.71');

      cy.get(`[data-cy="floatMinMika"]`).type('19271.03');
      cy.get(`[data-cy="floatMinMika"]`).should('have.value', '19271.03');

      cy.get(`[data-cy="floatMaxMika"]`).type('82.48');
      cy.get(`[data-cy="floatMaxMika"]`).should('have.value', '82.48');

      cy.get(`[data-cy="doubleRequiredMika"]`).type('29079.3');
      cy.get(`[data-cy="doubleRequiredMika"]`).should('have.value', '29079.3');

      cy.get(`[data-cy="doubleMinMika"]`).type('31950.35');
      cy.get(`[data-cy="doubleMinMika"]`).should('have.value', '31950.35');

      cy.get(`[data-cy="doubleMaxMika"]`).type('48.53');
      cy.get(`[data-cy="doubleMaxMika"]`).should('have.value', '48.53');

      cy.get(`[data-cy="bigDecimalRequiredMika"]`).type('41.45');
      cy.get(`[data-cy="bigDecimalRequiredMika"]`).should('have.value', '41.45');

      cy.get(`[data-cy="bigDecimalMinMika"]`).type('28388.41');
      cy.get(`[data-cy="bigDecimalMinMika"]`).should('have.value', '28388.41');

      cy.get(`[data-cy="bigDecimalMaxMika"]`).type('98.12');
      cy.get(`[data-cy="bigDecimalMaxMika"]`).should('have.value', '98.12');

      cy.get(`[data-cy="localDateMika"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateMika"]`).blur();
      cy.get(`[data-cy="localDateMika"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredMika"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateRequiredMika"]`).blur();
      cy.get(`[data-cy="localDateRequiredMika"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="instantMika"]`).type('2016-02-08T18:26');
      cy.get(`[data-cy="instantMika"]`).blur();
      cy.get(`[data-cy="instantMika"]`).should('have.value', '2016-02-08T18:26');

      cy.get(`[data-cy="instanteRequiredMika"]`).type('2016-02-08T02:38');
      cy.get(`[data-cy="instanteRequiredMika"]`).blur();
      cy.get(`[data-cy="instanteRequiredMika"]`).should('have.value', '2016-02-08T02:38');

      cy.get(`[data-cy="zonedDateTimeMika"]`).type('2016-02-07T21:14');
      cy.get(`[data-cy="zonedDateTimeMika"]`).blur();
      cy.get(`[data-cy="zonedDateTimeMika"]`).should('have.value', '2016-02-07T21:14');

      cy.get(`[data-cy="zonedDateTimeRequiredMika"]`).type('2016-02-07T23:58');
      cy.get(`[data-cy="zonedDateTimeRequiredMika"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredMika"]`).should('have.value', '2016-02-07T23:58');

      cy.get(`[data-cy="localTimeMika"]`).type('23:28:00');
      cy.get(`[data-cy="localTimeMika"]`).invoke('val').should('match', new RegExp('23:28:00'));

      cy.get(`[data-cy="localTimeRequiredMika"]`).type('02:13:00');
      cy.get(`[data-cy="localTimeRequiredMika"]`).invoke('val').should('match', new RegExp('02:13:00'));

      cy.get(`[data-cy="durationMika"]`).type('PT43M');
      cy.get(`[data-cy="durationMika"]`).blur();
      cy.get(`[data-cy="durationMika"]`).should('have.value', 'PT43M');

      cy.get(`[data-cy="durationRequiredMika"]`).type('PT29M');
      cy.get(`[data-cy="durationRequiredMika"]`).blur();
      cy.get(`[data-cy="durationRequiredMika"]`).should('have.value', 'PT29M');

      cy.get(`[data-cy="booleanMika"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanMika"]`).click();
      cy.get(`[data-cy="booleanMika"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredMika"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredMika"]`).click();
      cy.get(`[data-cy="booleanRequiredMika"]`).should('be.checked');

      cy.get(`[data-cy="enumMika"]`).select('ENUM_VALUE_1');

      cy.get(`[data-cy="enumRequiredMika"]`).select('ENUM_VALUE_3');

      cy.get(`[data-cy="uuidMika"]`).type('593c2cb7-bda2-428d-9247-6c5007c191cf');
      cy.get(`[data-cy="uuidMika"]`).invoke('val').should('match', new RegExp('593c2cb7-bda2-428d-9247-6c5007c191cf'));

      cy.get(`[data-cy="uuidRequiredMika"]`).type('25bd1456-34ed-44e0-a0ec-63ec25bac07e');
      cy.get(`[data-cy="uuidRequiredMika"]`).invoke('val').should('match', new RegExp('25bd1456-34ed-44e0-a0ec-63ec25bac07e'));

      cy.setFieldImageAsBytesOfEntity('byteImageMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesMika', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesMika', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextMika"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextMika"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredMika"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredMika"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestServiceImplEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestServiceImplEntityPageUrl);
    });
  });
});
