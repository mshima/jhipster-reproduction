import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EntityWithServiceClassAndPaginationDetail } from './entity-with-service-class-and-pagination-detail';

describe('EntityWithServiceClassAndPagination Management Detail Component', () => {
  let comp: EntityWithServiceClassAndPaginationDetail;
  let fixture: ComponentFixture<EntityWithServiceClassAndPaginationDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./entity-with-service-class-and-pagination-detail').then(m => m.EntityWithServiceClassAndPaginationDetail),
              resolve: { entityWithServiceClassAndPagination: () => of({ id: 11699 }) },
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
    fixture = TestBed.createComponent(EntityWithServiceClassAndPaginationDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load entityWithServiceClassAndPagination on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EntityWithServiceClassAndPaginationDetail);

      // THEN
      expect(instance.entityWithServiceClassAndPagination()).toEqual(expect.objectContaining({ id: 11699 }));
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
