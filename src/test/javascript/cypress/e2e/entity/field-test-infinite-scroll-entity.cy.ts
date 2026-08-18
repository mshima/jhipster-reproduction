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

describe('FieldTestInfiniteScrollEntity e2e test', () => {
  const fieldTestInfiniteScrollEntityPageUrl = '/field-test-infinite-scroll-entity';
  let username: string;
  let password: string;
  const fieldTestInfiniteScrollEntitySample = {
    stringRequiredHugo: 'character ah',
    integerRequiredHugo: 13406,
    longRequiredHugo: 29181,
    floatRequiredHugo: 11672.45,
    doubleRequiredHugo: 6153.78,
    bigDecimalRequiredHugo: 17874.14,
    localDateRequiredHugo: '2016-02-08',
    instanteRequiredHugo: '2016-02-08T03:39:43.454Z',
    zonedDateTimeRequiredHugo: '2016-02-08T02:27:43.964Z',
    localTimeRequiredHugo: '03:33:00',
    durationRequiredHugo: 734,
    booleanRequiredHugo: true,
    enumRequiredHugo: 'ENUM_VALUE_2',
    uuidRequiredHugo: '377f93ba-e7b1-4b84-b36c-1703e0219ebb',
    byteImageRequiredHugo: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteImageRequiredHugoContentType: 'unknown',
    byteAnyRequiredHugo: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    byteAnyRequiredHugoContentType: 'unknown',
    byteTextRequiredHugo: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let fieldTestInfiniteScrollEntity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/field-test-infinite-scroll-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/field-test-infinite-scroll-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/field-test-infinite-scroll-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fieldTestInfiniteScrollEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/field-test-infinite-scroll-entities/${fieldTestInfiniteScrollEntity.id}`,
      }).then(() => {
        fieldTestInfiniteScrollEntity = undefined;
      });
    }
  });

  it('FieldTestInfiniteScrollEntities menu should load FieldTestInfiniteScrollEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('field-test-infinite-scroll-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FieldTestInfiniteScrollEntity').should('exist');
    cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
  });

  describe('FieldTestInfiniteScrollEntity page', () => {
    it('should have translated page title', () => {
      cy.visit(fieldTestInfiniteScrollEntityPageUrl);
      cy.getEntityHeading('FieldTestInfiniteScrollEntity').should(
        'not.contain',
        'sampleWebfluxPsqlApp.fieldTestInfiniteScrollEntity.home.title',
      );
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fieldTestInfiniteScrollEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FieldTestInfiniteScrollEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fieldTestInfiniteScrollEntityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FieldTestInfiniteScrollEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/field-test-infinite-scroll-entities',
          body: fieldTestInfiniteScrollEntitySample,
        }).then(({ body }) => {
          fieldTestInfiniteScrollEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/field-test-infinite-scroll-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/field-test-infinite-scroll-entities?page=0&size=20>; rel="last",<http://localhost/api/field-test-infinite-scroll-entities?page=0&size=20>; rel="first"',
              },
              body: [fieldTestInfiniteScrollEntity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fieldTestInfiniteScrollEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FieldTestInfiniteScrollEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fieldTestInfiniteScrollEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
      });

      it('edit button click should load edit FieldTestInfiniteScrollEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestInfiniteScrollEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
      });

      it('edit button click should load edit FieldTestInfiniteScrollEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FieldTestInfiniteScrollEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
      });

      it('last delete button click should delete instance of FieldTestInfiniteScrollEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fieldTestInfiniteScrollEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);

        fieldTestInfiniteScrollEntity = undefined;
      });
    });
  });

  describe('new FieldTestInfiniteScrollEntity page', () => {
    beforeEach(() => {
      cy.visit(fieldTestInfiniteScrollEntityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FieldTestInfiniteScrollEntity');
    });

    it('should create an instance of FieldTestInfiniteScrollEntity', () => {
      cy.get(`[data-cy="stringHugo"]`).type('suitcase');
      cy.get(`[data-cy="stringHugo"]`).should('have.value', 'suitcase');

      cy.get(`[data-cy="stringRequiredHugo"]`).type('innovate sediment quietly');
      cy.get(`[data-cy="stringRequiredHugo"]`).should('have.value', 'innovate sediment quietly');

      cy.get(`[data-cy="stringMinlengthHugo"]`).type('prickly');
      cy.get(`[data-cy="stringMinlengthHugo"]`).should('have.value', 'prickly');

      cy.get(`[data-cy="stringMaxlengthHugo"]`).type('wherever viciously c');
      cy.get(`[data-cy="stringMaxlengthHugo"]`).should('have.value', 'wherever viciously c');

      cy.get(`[data-cy="stringPatternHugo"]`).type('KWX');
      cy.get(`[data-cy="stringPatternHugo"]`).should('have.value', 'KWX');

      cy.get(`[data-cy="integerHugo"]`).type('23239');
      cy.get(`[data-cy="integerHugo"]`).should('have.value', '23239');

      cy.get(`[data-cy="integerRequiredHugo"]`).type('20704');
      cy.get(`[data-cy="integerRequiredHugo"]`).should('have.value', '20704');

      cy.get(`[data-cy="integerMinHugo"]`).type('22712');
      cy.get(`[data-cy="integerMinHugo"]`).should('have.value', '22712');

      cy.get(`[data-cy="integerMaxHugo"]`).type('98');
      cy.get(`[data-cy="integerMaxHugo"]`).should('have.value', '98');

      cy.get(`[data-cy="longHugo"]`).type('15177');
      cy.get(`[data-cy="longHugo"]`).should('have.value', '15177');

      cy.get(`[data-cy="longRequiredHugo"]`).type('27910');
      cy.get(`[data-cy="longRequiredHugo"]`).should('have.value', '27910');

      cy.get(`[data-cy="longMinHugo"]`).type('5919');
      cy.get(`[data-cy="longMinHugo"]`).should('have.value', '5919');

      cy.get(`[data-cy="longMaxHugo"]`).type('82');
      cy.get(`[data-cy="longMaxHugo"]`).should('have.value', '82');

      cy.get(`[data-cy="floatHugo"]`).type('7132.51');
      cy.get(`[data-cy="floatHugo"]`).should('have.value', '7132.51');

      cy.get(`[data-cy="floatRequiredHugo"]`).type('529.44');
      cy.get(`[data-cy="floatRequiredHugo"]`).should('have.value', '529.44');

      cy.get(`[data-cy="floatMinHugo"]`).type('3595.19');
      cy.get(`[data-cy="floatMinHugo"]`).should('have.value', '3595.19');

      cy.get(`[data-cy="floatMaxHugo"]`).type('86.62');
      cy.get(`[data-cy="floatMaxHugo"]`).should('have.value', '86.62');

      cy.get(`[data-cy="doubleRequiredHugo"]`).type('4110.31');
      cy.get(`[data-cy="doubleRequiredHugo"]`).should('have.value', '4110.31');

      cy.get(`[data-cy="doubleMinHugo"]`).type('2327.49');
      cy.get(`[data-cy="doubleMinHugo"]`).should('have.value', '2327.49');

      cy.get(`[data-cy="doubleMaxHugo"]`).type('10.01');
      cy.get(`[data-cy="doubleMaxHugo"]`).should('have.value', '10.01');

      cy.get(`[data-cy="bigDecimalRequiredHugo"]`).type('10845.15');
      cy.get(`[data-cy="bigDecimalRequiredHugo"]`).should('have.value', '10845.15');

      cy.get(`[data-cy="bigDecimalMinHugo"]`).type('7677.42');
      cy.get(`[data-cy="bigDecimalMinHugo"]`).should('have.value', '7677.42');

      cy.get(`[data-cy="bigDecimalMaxHugo"]`).type('85.77');
      cy.get(`[data-cy="bigDecimalMaxHugo"]`).should('have.value', '85.77');

      cy.get(`[data-cy="localDateHugo"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateHugo"]`).blur();
      cy.get(`[data-cy="localDateHugo"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="localDateRequiredHugo"]`).type('2016-02-08');
      cy.get(`[data-cy="localDateRequiredHugo"]`).blur();
      cy.get(`[data-cy="localDateRequiredHugo"]`).should('have.value', '2016-02-08');

      cy.get(`[data-cy="instantHugo"]`).type('2016-02-08T16:01');
      cy.get(`[data-cy="instantHugo"]`).blur();
      cy.get(`[data-cy="instantHugo"]`).should('have.value', '2016-02-08T16:01');

      cy.get(`[data-cy="instanteRequiredHugo"]`).type('2016-02-07T22:15');
      cy.get(`[data-cy="instanteRequiredHugo"]`).blur();
      cy.get(`[data-cy="instanteRequiredHugo"]`).should('have.value', '2016-02-07T22:15');

      cy.get(`[data-cy="zonedDateTimeHugo"]`).type('2016-02-08T08:05');
      cy.get(`[data-cy="zonedDateTimeHugo"]`).blur();
      cy.get(`[data-cy="zonedDateTimeHugo"]`).should('have.value', '2016-02-08T08:05');

      cy.get(`[data-cy="zonedDateTimeRequiredHugo"]`).type('2016-02-07T23:30');
      cy.get(`[data-cy="zonedDateTimeRequiredHugo"]`).blur();
      cy.get(`[data-cy="zonedDateTimeRequiredHugo"]`).should('have.value', '2016-02-07T23:30');

      cy.get(`[data-cy="localTimeHugo"]`).type('07:57:00');
      cy.get(`[data-cy="localTimeHugo"]`).invoke('val').should('match', new RegExp('07:57:00'));

      cy.get(`[data-cy="localTimeRequiredHugo"]`).type('19:55:00');
      cy.get(`[data-cy="localTimeRequiredHugo"]`).invoke('val').should('match', new RegExp('19:55:00'));

      cy.get(`[data-cy="durationHugo"]`).type('PT16M');
      cy.get(`[data-cy="durationHugo"]`).blur();
      cy.get(`[data-cy="durationHugo"]`).should('have.value', 'PT16M');

      cy.get(`[data-cy="durationRequiredHugo"]`).type('PT47M');
      cy.get(`[data-cy="durationRequiredHugo"]`).blur();
      cy.get(`[data-cy="durationRequiredHugo"]`).should('have.value', 'PT47M');

      cy.get(`[data-cy="booleanHugo"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanHugo"]`).click();
      cy.get(`[data-cy="booleanHugo"]`).should('be.checked');

      cy.get(`[data-cy="booleanRequiredHugo"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanRequiredHugo"]`).click();
      cy.get(`[data-cy="booleanRequiredHugo"]`).should('be.checked');

      cy.get(`[data-cy="enumHugo"]`).select('ENUM_VALUE_2');

      cy.get(`[data-cy="enumRequiredHugo"]`).select('ENUM_VALUE_2');

      cy.get(`[data-cy="uuidHugo"]`).type('5ef0eb1e-c256-42fb-9272-65cd08c79664');
      cy.get(`[data-cy="uuidHugo"]`).invoke('val').should('match', new RegExp('5ef0eb1e-c256-42fb-9272-65cd08c79664'));

      cy.get(`[data-cy="uuidRequiredHugo"]`).type('d9a4291c-ff96-4c64-b959-9abb88dd8074');
      cy.get(`[data-cy="uuidRequiredHugo"]`).invoke('val').should('match', new RegExp('d9a4291c-ff96-4c64-b959-9abb88dd8074'));

      cy.setFieldImageAsBytesOfEntity('byteImageHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageRequiredHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMinbytesHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteImageMaxbytesHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyRequiredHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMinbytesHugo', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('byteAnyMaxbytesHugo', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="byteTextHugo"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextHugo"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="byteTextRequiredHugo"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="byteTextRequiredHugo"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fieldTestInfiniteScrollEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fieldTestInfiniteScrollEntityPageUrl);
    });
  });
});
