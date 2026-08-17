import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CustomPackageParentDetail } from './custom-package-parent-detail';

describe('CustomPackageParent Management Detail Component', () => {
  let comp: CustomPackageParentDetail;
  let fixture: ComponentFixture<CustomPackageParentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./custom-package-parent-detail').then(m => m.CustomPackageParentDetail),
              resolve: { customPackageParent: () => of({ id: 26792 }) },
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
    fixture = TestBed.createComponent(CustomPackageParentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load customPackageParent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustomPackageParentDetail);

      // THEN
      expect(instance.customPackageParent()).toEqual(expect.objectContaining({ id: 26792 }));
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
