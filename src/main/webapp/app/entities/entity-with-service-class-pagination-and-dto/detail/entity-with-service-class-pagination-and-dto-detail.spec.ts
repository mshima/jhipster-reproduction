import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EntityWithServiceClassPaginationAndDTODetail } from './entity-with-service-class-pagination-and-dto-detail';

describe('EntityWithServiceClassPaginationAndDTO Management Detail Component', () => {
  let comp: EntityWithServiceClassPaginationAndDTODetail;
  let fixture: ComponentFixture<EntityWithServiceClassPaginationAndDTODetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./entity-with-service-class-pagination-and-dto-detail').then(m => m.EntityWithServiceClassPaginationAndDTODetail),
              resolve: { entityWithServiceClassPaginationAndDTO: () => of({ id: 484 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityWithServiceClassPaginationAndDTODetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load entityWithServiceClassPaginationAndDTO on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EntityWithServiceClassPaginationAndDTODetail);

      // THEN
      expect(instance.entityWithServiceClassPaginationAndDTO()).toEqual(expect.objectContaining({ id: 484 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
