import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestServiceImplEntityFormService } from './field-test-service-impl-entity-form.service';
import { FieldTestServiceImplEntityService } from '../service/field-test-service-impl-entity.service';
import { IFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';

import { FieldTestServiceImplEntityUpdate } from './field-test-service-impl-entity-update';

describe('FieldTestServiceImplEntity Management Update Component', () => {
  let comp: FieldTestServiceImplEntityUpdate;
  let fixture: ComponentFixture<FieldTestServiceImplEntityUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestServiceImplEntityFormService: FieldTestServiceImplEntityFormService;
  let fieldTestServiceImplEntityService: FieldTestServiceImplEntityService;

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

    fixture = TestBed.createComponent(FieldTestServiceImplEntityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestServiceImplEntityFormService = TestBed.inject(FieldTestServiceImplEntityFormService);
    fieldTestServiceImplEntityService = TestBed.inject(FieldTestServiceImplEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestServiceImplEntity: IFieldTestServiceImplEntity = { id: 7459 };

      activatedRoute.data = of({ fieldTestServiceImplEntity });
      comp.ngOnInit();

      expect(comp.fieldTestServiceImplEntity).toEqual(fieldTestServiceImplEntity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceImplEntity>();
      const fieldTestServiceImplEntity = { id: 20444 };
      vi.spyOn(fieldTestServiceImplEntityFormService, 'getFieldTestServiceImplEntity').mockReturnValue(fieldTestServiceImplEntity);
      vi.spyOn(fieldTestServiceImplEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceImplEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestServiceImplEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestServiceImplEntityFormService.getFieldTestServiceImplEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestServiceImplEntityService.update).toHaveBeenCalledWith(expect.objectContaining(fieldTestServiceImplEntity));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceImplEntity>();
      const fieldTestServiceImplEntity = { id: 20444 };
      vi.spyOn(fieldTestServiceImplEntityFormService, 'getFieldTestServiceImplEntity').mockReturnValue({ id: null });
      vi.spyOn(fieldTestServiceImplEntityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceImplEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestServiceImplEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestServiceImplEntityFormService.getFieldTestServiceImplEntity).toHaveBeenCalled();
      expect(fieldTestServiceImplEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceImplEntity>();
      const fieldTestServiceImplEntity = { id: 20444 };
      vi.spyOn(fieldTestServiceImplEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceImplEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestServiceImplEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
