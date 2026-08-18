import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EntityWithServiceImplAndDTODetail } from './entity-with-service-impl-and-dto-detail';

describe('EntityWithServiceImplAndDTO Management Detail Component', () => {
  let comp: EntityWithServiceImplAndDTODetail;
  let fixture: ComponentFixture<EntityWithServiceImplAndDTODetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./entity-with-service-impl-and-dto-detail').then(m => m.EntityWithServiceImplAndDTODetail),
              resolve: { entityWithServiceImplAndDTO: () => of({ id: 30188 }) },
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
    fixture = TestBed.createComponent(EntityWithServiceImplAndDTODetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load entityWithServiceImplAndDTO on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EntityWithServiceImplAndDTODetail);

      // THEN
      expect(instance.entityWithServiceImplAndDTO()).toEqual(expect.objectContaining({ id: 30188 }));
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
