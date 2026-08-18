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

describe('FieldTestEntity e2e test', () => {
  const fieldTestEntityPageUrl = '/field-test-entity';
  let username: string;
  let password: string;
  const fieldTestEntitySample = {
    stringRequiredTom: 'reference',
    numberPatternRequiredTom: '67',
    integerRequiredTom: 30395,
    longRequiredTom: 17042,
    floatRequiredTom: 21858.44,
    doubleRequiredTom: 16458.89,
    bigDecimalRequiredTom: 7316.91,
    localDateRequiredTom: '2016-02-08',
    instantRequiredTom: '2016-02-08T04:21:28.104Z',
    zonedDateTimeRequiredTom: '2016-02-08T16:38:30.947Z',
    localTimeRequiredTom: '01:50:00',
    durationRequiredTom: 401,
    booleanRequiredTom: false,
    enumRequiredTom: 'ENUM_VALUE_3',
    uuidRequiredTom: '44661f7f-a3d7-4dc2-9389-976c356dbacf',
    byteImageRequiredTom: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredTomContentType: 'unknown',
    byteAnyRequiredTom: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredTomContentType: 'unknown',
    byteTextRequiredTom: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-entities/${fieldTestEntity.id}`,
      }).then(() => {
        fieldTestEntity = undefined;
      });
    }
  });

  it('FieldTestEntities menu should load FieldTestEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestEntityPageUrl);
  });

  describe('FieldTestEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestEntityPageUrl);
      cy.getEntityHeading('FieldTestEntity').should('not.contain', 'sampleWebfluxPsqlApp.fieldTestEntity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-entities',
          body: fieldTestEntitySample,
        }).then(({ body }) => {
          fieldTestEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fieldTestEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEntityPageUrl);
      });

      it('edit button click should load edit FieldTestEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEntityPageUrl);
      });

      it('edit button click should load edit FieldTestEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestEntityPageUrl);

        fieldTestEntity = undefined;
      });
    });
  });

  describe('new FieldTestEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestEntity');
    });

    it('should create an instance of FieldTestEntity', () => {
      cy.get(`[data-cy="stringTom"]`).type('surprisingly');
      cy.get(`[data-cy="stringTom"]`).should('have.value', 'surprisingly');

      cy.get(`[data-cy="stringRequiredTom"]`).type('offend synergy');
      cy.get(`[data-cy="stringRequiredTom"]`).should('have.value', 'offend synergy');

      cy.get(`[data-cy="stringMinlengthTom"]`).type('indeed');
      cy.get(`[data-cy="stringMinlengthTom"]`).should('have.value', 'indeed');

      cy.get(`[data-cy="stringMaxlengthTom"]`).type('accidentally');
      cy.get(`[data-cy="stringMaxlengthTom"]`).should('have.value', 'accidentally');

      cy.get(`[data-cy="stringPatternTom"]`).type('qL0');
      cy.get(`[data-cy="stringPatternTom"]`).should('have.value', 'qL0');

      cy.get(`[data-cy="numberPatternTom"]`).type('506469');
      cy.get(`[data-cy="numberPatternTom"]`).should('have.value', '506469');

      cy.get(`[data-cy="numberPatternRequiredTom"]`).type('57825');
      cy.get(`[data-cy="numberPatternRequiredTom"]`).should('have.value', '57825');

      cy.get(`[data-cy="integerTom"]`).type('3570');
      cy.get(`[data-cy="integerTom"]`).should('have.value', '3570');

      cy.get(`[data-cy="integerRequiredTom"]`).type('25269');
      cy.get(`[data-cy="integerRequiredTom"]`).should('have.value', '25269');

      cy.get(`[data-cy="integerMinTom"]`).type('21236');
      cy.get(`[data-cy="integerMinTom"]`).should('have.value', '21236');

      cy.get(`[data-cy="integerMaxTom"]`).type('1');
      cy.get(`[data-cy="integerMaxTom"]`).should('have.value', '1');

      cy.get(`[data-cy="longTom"]`).type('29153');
      cy.get(`[data-cy="longTom"]`).should('have.value', '29153');

      cy.get(`[data-cy="longRequiredTom"]`).type('14245');
      cy.get(`[data-cy="longRequiredTom"]`).should('have.value', '14245');

      cy.get(`[data-cy="longMinTom"]`).type('28');
      cy.get(`[data-cy="longMinTom"]`).should('have.value', '28');

      cy.get(`[data-cy="longMaxTom"]`).type('54');
      cy.get(`[data-cy="longMaxTom"]`).should('have.value', '54');

      cy.get(`[data-cy="floatTom"]`).type('2652.3');
      cy.get(`[data-cy="floatTom"]`).should('have.value', '2652.3');

      cy.get(`[data-cy="floatRequiredTom"]`).type('8222.73');
      cy.get(`[data-cy="floatRequiredTom"]`).should('have.value', '8222.73');

      cy.get(`[data-cy="floatMinTom"]`).type('28954.02');
      cy.get(`[data-cy="floatMinTom"]`).should('have.value', '28954.02');

      cy.get(`[data-cy="floatMaxTom"]`).type('58.76');
      cy.get(`[data-cy="floatMaxTom"]`).should('have.value', '58.76');

      cy.get(`[data-cy="doubleRequiredTom"]`).type('1141.3');
      cy.get(`[data-cy="doubleRequiredTom"]`).should('have.value', '1141.3');

      cy.get(`[data-cy="doubleMinTom"]`).type('5719.05');
      cy.get(`[data-cy="doubleMinTom"]`).should('have.value', '5719.05');

      cy.get(`[data-cy="doubleMaxTom"]`).type('59.23');
      cy.get(`[data-cy="doubleMaxTom"]`).should('have.value', '59.23');

      cy.get(`[data-cy="bigDecimalRequiredTom"]`).type('3880.79');
      cy.get(`[data-cy="bigDecimalRequiredTom"]`).should('have.value', '3880.79');

      cy.get(`[data-cy="bigDecimalMinTom"]`).type('7292.16');
      cy.get(`[data-cy="bigDecimalMinTom"]`).should('have.value', '7292.16');

      cy.get(`[data-cy="bigDecimalMaxTom"]`).type('86.28');
      cy.get(`[data-cy="bigDecimalMaxTom"]`).should('have.value', '86.28');

      cy.get(`[data-cy="localDateTom"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateTom"]`).blur();
      cy.get(`[data-cy="localDateTom"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredTom"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateRequiredTom"]`).blur();
      cy.get(`[data-cy="localDateRequiredTom"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="instantTom"]`).type('2016-02-08T00:58');
      cy.get(`[data-cy="instantTom"]`).blur();
      cy.get(`[data-cy="instantTom"]`).should('have.value', '2016-02-08T00:58');

      cy.get(`[data-cy="instantRequiredTom"]`).type('2016-02-08T09:19');
      cy.get(`[data-cy="instantRequiredTom"]`).blur();
      cy.get(`[data-cy="instantRequiredTom"]`).should('have.value', '2016-02-08T09:19');

      cy.get(`[data-cy="zonedDateTimeTom"]`).type('2016-02-08T12:27');
      cy.get(`[data-cy="zonedDateTimeTom"]`).blur();
      cy.get(`[data-cy="zonedDateTimeTom"]`).should('have.value', '2016-02-08T12:27');

      cy.get(`[data-cy="zonedDateTimeRequiredTom"]`).type('2016-02-08T08:41');
      cy.get(`[data-cy="zonedDateTimeRequiredTom"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredTom"]`).should('have.value', '2016-02-08T08:41');

      cy.get(`[data-cy="localTimeTom"]`).type('03:50:00');
      cy.get(`[data-cy="localTimeTom"]`).invoke('val').should('match', new RegExp('03:50:00'));

      cy.get(`[data-cy="localTimeRequiredTom"]`).type('08:30:00');
      cy.get(`[data-cy="localTimeRequiredTom"]`).invoke('val').should('match', new RegExp('08:30:00'));

      cy.get(`[data-cy="durationTom"]`).type('PT56M');
      cy.get(`[data-cy="durationTom"]`).blur();
      cy.get(`[data-cy="durationTom"]`).should('have.value', 'PT56M');

      cy.get(`[data-cy="durationRequiredTom"]`).type('PT21M');
      cy.get(`[data-cy="durationRequiredTom"]`).blur();
      cy.get(`[data-cy="durationRequiredTom"]`).should('have.value', 'PT21M');

      cy.get(`[data-cy="booleanTom"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanTom"]`).click();
      cy.get(`[data-cy="booleanTom"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredTom"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredTom"]`).click();
      cy.get(`[data-cy="booleanRequiredTom"]`).should('be.checked');

      cy.get(`[data-cy="enumTom"]`).select('ENUM_VALUE_3');

      cy.get(`[data-cy="enumRequiredTom"]`).select('ENUM_VALUE_3');

      cy.get(`[data-cy="uuidTom"]`).type('014fc03b-cb88-4f97-9c6f-ef62e1041ef9');
      cy.get(`[data-cy="uuidTom"]`).invoke('val').should('match', new RegExp('014fc03b-cb88-4f97-9c6f-ef62e1041ef9'));

      cy.get(`[data-cy="uuidRequiredTom"]`).type('0addb4fe-2002-4287-84b4-0385dbc7d6af');
      cy.get(`[data-cy="uuidRequiredTom"]`).invoke('val').should('match', new RegExp('0addb4fe-2002-4287-84b4-0385dbc7d6af'));

      cy.setFieldImageAsBytesOfEntity('byteImageTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesTom', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesTom', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextTom"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextTom"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredTom"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredTom"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestEntityPageUrl);
    });
  });
});
