import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EntityWithServiceImplAndPaginationDetail } from './entity-with-service-impl-and-pagination-detail';

describe('EntityWithServiceImplAndPagination Management Detail Component', () => {
  let comp: EntityWithServiceImplAndPaginationDetail;
  let fixture: ComponentFixture<EntityWithServiceImplAndPaginationDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./entity-with-service-impl-and-pagination-detail').then(m => m.EntityWithServiceImplAndPaginationDetail),
              resolve: { entityWithServiceImplAndPagination: () => of({ id: 27409 }) },
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
    fixture = TestBed.createComponent(EntityWithServiceImplAndPaginationDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load entityWithServiceImplAndPagination on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EntityWithServiceImplAndPaginationDetail);

      // THEN
      expect(instance.entityWithServiceImplAndPagination()).toEqual(expect.objectContaining({ id: 27409 }));
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
