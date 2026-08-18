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

describe('MapsIdUserProfileWithDTO e2e test', () => {
  const mapsIdUserProfileWithDTOPageUrl = '/maps-id-user-profile-with-dto';
  let username: string;
  let password: string;
  // const mapsIdUserProfileWithDTOSample = {"dateOfBirth":"2019-01-16T19:30:40.871Z"};

  let mapsIdUserProfileWithDTO;
  // let user;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"Jaylan.Kovacek62","firstName":"Pam","lastName":"Hettinger","email":"Jordane.Russel@yahoo.com","langKey":"briskly","imageUrl":"fat front unlawful"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/maps-id-user-profile-with-dtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/maps-id-user-profile-with-dtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/maps-id-user-profile-with-dtos/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (mapsIdUserProfileWithDTO) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/maps-id-user-profile-with-dtos/${mapsIdUserProfileWithDTO.id}`,
      }).then(() => {
        mapsIdUserProfileWithDTO = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('MapsIdUserProfileWithDTOS menu should load MapsIdUserProfileWithDTOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('maps-id-user-profile-with-dto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MapsIdUserProfileWithDTO').should('exist');
    cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
  });

  describe('MapsIdUserProfileWithDTO page', () => {
    it('should have translated page title', () => {
      cy.visit(mapsIdUserProfileWithDTOPageUrl);
      cy.getEntityHeading('MapsIdUserProfileWithDTO').should('not.contain', 'sampleWebfluxPsqlApp.mapsIdUserProfileWithDTO.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mapsIdUserProfileWithDTOPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MapsIdUserProfileWithDTO page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${mapsIdUserProfileWithDTOPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('MapsIdUserProfileWithDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/maps-id-user-profile-with-dtos',
          body: {
            ...mapsIdUserProfileWithDTOSample,
            user: user,
          },
        }).then(({ body }) => {
          mapsIdUserProfileWithDTO = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/maps-id-user-profile-with-dtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mapsIdUserProfileWithDTO],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(mapsIdUserProfileWithDTOPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(mapsIdUserProfileWithDTOPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details MapsIdUserProfileWithDTO page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mapsIdUserProfileWithDTO');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
      });

      it('edit button click should load edit MapsIdUserProfileWithDTO page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MapsIdUserProfileWithDTO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
      });

      it('edit button click should load edit MapsIdUserProfileWithDTO page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MapsIdUserProfileWithDTO');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of MapsIdUserProfileWithDTO', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mapsIdUserProfileWithDTO').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);

        mapsIdUserProfileWithDTO = undefined;
      });
    });
  });

  describe('new MapsIdUserProfileWithDTO page', () => {
    beforeEach(() => {
      cy.visit(mapsIdUserProfileWithDTOPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MapsIdUserProfileWithDTO');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of MapsIdUserProfileWithDTO', () => {
      cy.get(`[data-cy="dateOfBirth"]`).type('2019-01-16T10:51');
      cy.get(`[data-cy="dateOfBirth"]`).blur();
      cy.get(`[data-cy="dateOfBirth"]`).should('have.value', '2019-01-16T10:51');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        mapsIdUserProfileWithDTO = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', mapsIdUserProfileWithDTOPageUrl);
    });
  });
});
