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

describe('BankAccount e2e test', () => {
  const bankAccountPageUrl = '/bank-account-my-suffix';
  let username: string;
  let password: string;
  const bankAccountSample = { name: 'quintessential incandescence', balance: 8815.46 };

  let bankAccount;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bank-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bank-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bank-accounts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bankAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-accounts/${bankAccount.id}`,
      }).then(() => {
        bankAccount = undefined;
      });
    }
  });

  it('BankAccounts menu should load BankAccounts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank-account-my-suffix');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BankAccount').should('exist');
    cy.location('pathname').should('eq', bankAccountPageUrl);
  });

  describe('BankAccount page', () => {
    it('should have translated page title', () => {
      cy.visit(bankAccountPageUrl);
      cy.getEntityHeading('BankAccount').should('not.contain', 'sampleWebfluxPsqlApp.testRootBankAccount.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankAccountPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BankAccount page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${bankAccountPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('BankAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bankAccountPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bank-accounts',
          body: bankAccountSample,
        }).then(({ body }) => {
          bankAccount = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bank-accounts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [bankAccount],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankAccountPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BankAccount page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bankAccount');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bankAccountPageUrl);
      });

      it('edit button click should load edit BankAccount page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bankAccountPageUrl);
      });

      it('edit button click should load edit BankAccount page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankAccount');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bankAccountPageUrl);
      });

      it('last delete button click should delete instance of BankAccount', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bankAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bankAccountPageUrl);

        bankAccount = undefined;
      });
    });
  });

  describe('new BankAccount page', () => {
    beforeEach(() => {
      cy.visit(bankAccountPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BankAccount');
    });

    it('should create an instance of BankAccount', () => {
      cy.get(`[data-cy="name"]`).type('forenenst fax');
      cy.get(`[data-cy="name"]`).should('have.value', 'forenenst fax');

      cy.get(`[data-cy="guid"]`).type('ea73c616-e43e-41b5-a219-dbb16d247be2');
      cy.get(`[data-cy="guid"]`).invoke('val').should('match', new RegExp('ea73c616-e43e-41b5-a219-dbb16d247be2'));

      cy.get(`[data-cy="bankNumber"]`).type('5567');
      cy.get(`[data-cy="bankNumber"]`).should('have.value', '5567');

      cy.get(`[data-cy="agencyNumber"]`).type('5760');
      cy.get(`[data-cy="agencyNumber"]`).should('have.value', '5760');

      cy.get(`[data-cy="lastOperationDuration"]`).type('7434.01');
      cy.get(`[data-cy="lastOperationDuration"]`).should('have.value', '7434.01');

      cy.get(`[data-cy="meanOperationDuration"]`).type('26783.35');
      cy.get(`[data-cy="meanOperationDuration"]`).should('have.value', '26783.35');

      cy.get(`[data-cy="meanQueueDuration"]`).type('PT40M');
      cy.get(`[data-cy="meanQueueDuration"]`).blur();
      cy.get(`[data-cy="meanQueueDuration"]`).should('have.value', 'PT40M');

      cy.get(`[data-cy="balance"]`).type('4104.73');
      cy.get(`[data-cy="balance"]`).should('have.value', '4104.73');

      cy.get(`[data-cy="openingDay"]`).type('2020-08-04');
      cy.get(`[data-cy="openingDay"]`).blur();
      cy.get(`[data-cy="openingDay"]`).should('have.value', '2020-08-04');

      cy.get(`[data-cy="lastOperationDate"]`).type('2020-08-04T00:33');
      cy.get(`[data-cy="lastOperationDate"]`).blur();
      cy.get(`[data-cy="lastOperationDate"]`).should('have.value', '2020-08-04T00:33');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="accountType"]`).select('CHECKING');

      cy.setFieldImageAsBytesOfEntity('attachment', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // The blob fields are validated asynchronously; wait for the form to become valid
      // (save button enabled) instead of a fixed delay before submitting.
      cy.get(entityCreateSaveButtonSelector).should('be.enabled');
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bankAccount = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', bankAccountPageUrl);
    });
  });
});
