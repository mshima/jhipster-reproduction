import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EntityWithServiceImplPaginationAndDTODetail } from './entity-with-service-impl-pagination-and-dto-detail';

describe('EntityWithServiceImplPaginationAndDTO Management Detail Component', () => {
  let comp: EntityWithServiceImplPaginationAndDTODetail;
  let fixture: ComponentFixture<EntityWithServiceImplPaginationAndDTODetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./entity-with-service-impl-pagination-and-dto-detail').then(m => m.EntityWithServiceImplPaginationAndDTODetail),
              resolve: { entityWithServiceImplPaginationAndDTO: () => of({ id: 19834 }) },
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
    fixture = TestBed.createComponent(EntityWithServiceImplPaginationAndDTODetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load entityWithServiceImplPaginationAndDTO on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EntityWithServiceImplPaginationAndDTODetail);

      // THEN
      expect(instance.entityWithServiceImplPaginationAndDTO()).toEqual(expect.objectContaining({ id: 19834 }));
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
