import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FieldTestEnumWithValueDetail } from './field-test-enum-with-value-detail';

describe('FieldTestEnumWithValue Management Detail Component', () => {
  let comp: FieldTestEnumWithValueDetail;
  let fixture: ComponentFixture<FieldTestEnumWithValueDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./field-test-enum-with-value-detail').then(m => m.FieldTestEnumWithValueDetail),
              resolve: { fieldTestEnumWithValue: () => of({ id: 6834 }) },
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
    fixture = TestBed.createComponent(FieldTestEnumWithValueDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load fieldTestEnumWithValue on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FieldTestEnumWithValueDetail);

      // THEN
      expect(instance.fieldTestEnumWithValue()).toEqual(expect.objectContaining({ id: 6834 }));
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
