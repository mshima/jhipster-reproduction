import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestEnumWithValueFormService } from './field-test-enum-with-value-form.service';
import { FieldTestEnumWithValueService } from '../service/field-test-enum-with-value.service';
import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';

import { FieldTestEnumWithValueUpdate } from './field-test-enum-with-value-update';

describe('FieldTestEnumWithValue Management Update Component', () => {
  let comp: FieldTestEnumWithValueUpdate;
  let fixture: ComponentFixture<FieldTestEnumWithValueUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestEnumWithValueFormService: FieldTestEnumWithValueFormService;
  let fieldTestEnumWithValueService: FieldTestEnumWithValueService;

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

    fixture = TestBed.createComponent(FieldTestEnumWithValueUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestEnumWithValueFormService = TestBed.inject(FieldTestEnumWithValueFormService);
    fieldTestEnumWithValueService = TestBed.inject(FieldTestEnumWithValueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestEnumWithValue: IFieldTestEnumWithValue = { id: 1054 };

      activatedRoute.data = of({ fieldTestEnumWithValue });
      comp.ngOnInit();

      expect(comp.fieldTestEnumWithValue).toEqual(fieldTestEnumWithValue);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestEnumWithValue>();
      const fieldTestEnumWithValue = { id: 6834 };
      vi.spyOn(fieldTestEnumWithValueFormService, 'getFieldTestEnumWithValue').mockReturnValue(fieldTestEnumWithValue);
      vi.spyOn(fieldTestEnumWithValueService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestEnumWithValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestEnumWithValue);
      saveSubject.complete();

      // THEN
      expect(fieldTestEnumWithValueFormService.getFieldTestEnumWithValue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestEnumWithValueService.update).toHaveBeenCalledWith(expect.objectContaining(fieldTestEnumWithValue));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestEnumWithValue>();
      const fieldTestEnumWithValue = { id: 6834 };
      vi.spyOn(fieldTestEnumWithValueFormService, 'getFieldTestEnumWithValue').mockReturnValue({ id: null });
      vi.spyOn(fieldTestEnumWithValueService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestEnumWithValue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestEnumWithValue);
      saveSubject.complete();

      // THEN
      expect(fieldTestEnumWithValueFormService.getFieldTestEnumWithValue).toHaveBeenCalled();
      expect(fieldTestEnumWithValueService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestEnumWithValue>();
      const fieldTestEnumWithValue = { id: 6834 };
      vi.spyOn(fieldTestEnumWithValueService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestEnumWithValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestEnumWithValueService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
