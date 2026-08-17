import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustomPackageParent } from '../custom-package-parent.model';
import { CustomPackageParentService } from '../service/custom-package-parent.service';

import { CustomPackageParentFormService } from './custom-package-parent-form.service';
import { CustomPackageParentUpdate } from './custom-package-parent-update';

describe('CustomPackageParent Management Update Component', () => {
  let comp: CustomPackageParentUpdate;
  let fixture: ComponentFixture<CustomPackageParentUpdate>;
  let activatedRoute: ActivatedRoute;
  let customPackageParentFormService: CustomPackageParentFormService;
  let customPackageParentService: CustomPackageParentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CustomPackageParentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customPackageParentFormService = TestBed.inject(CustomPackageParentFormService);
    customPackageParentService = TestBed.inject(CustomPackageParentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const customPackageParent: ICustomPackageParent = { id: 3080 };

      activatedRoute.data = of({ customPackageParent });
      comp.ngOnInit();

      expect(comp.customPackageParent).toEqual(customPackageParent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageParent>();
      const customPackageParent = { id: 26792 };
      vi.spyOn(customPackageParentFormService, 'getCustomPackageParent').mockReturnValue(customPackageParent);
      vi.spyOn(customPackageParentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageParent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customPackageParent);
      saveSubject.complete();

      // THEN
      expect(customPackageParentFormService.getCustomPackageParent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customPackageParentService.update).toHaveBeenCalledWith(expect.objectContaining(customPackageParent));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageParent>();
      const customPackageParent = { id: 26792 };
      vi.spyOn(customPackageParentFormService, 'getCustomPackageParent').mockReturnValue({ id: null });
      vi.spyOn(customPackageParentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageParent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customPackageParent);
      saveSubject.complete();

      // THEN
      expect(customPackageParentFormService.getCustomPackageParent).toHaveBeenCalled();
      expect(customPackageParentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageParent>();
      const customPackageParent = { id: 26792 };
      vi.spyOn(customPackageParentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageParent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customPackageParentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
