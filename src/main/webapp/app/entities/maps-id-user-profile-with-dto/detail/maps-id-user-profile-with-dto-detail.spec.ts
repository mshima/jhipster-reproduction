import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { MapsIdUserProfileWithDTODetail } from './maps-id-user-profile-with-dto-detail';

describe('MapsIdUserProfileWithDTO Management Detail Component', () => {
  let comp: MapsIdUserProfileWithDTODetail;
  let fixture: ComponentFixture<MapsIdUserProfileWithDTODetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./maps-id-user-profile-with-dto-detail').then(m => m.MapsIdUserProfileWithDTODetail),
              resolve: { mapsIdUserProfileWithDTO: () => of({ id: 8289 }) },
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
    fixture = TestBed.createComponent(MapsIdUserProfileWithDTODetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load mapsIdUserProfileWithDTO on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MapsIdUserProfileWithDTODetail);

      // THEN
      expect(instance.mapsIdUserProfileWithDTO()).toEqual(expect.objectContaining({ id: 8289 }));
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
