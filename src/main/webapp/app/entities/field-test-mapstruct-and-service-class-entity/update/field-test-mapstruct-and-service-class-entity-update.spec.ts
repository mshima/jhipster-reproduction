import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestMapstructAndServiceClassEntityFormService } from './field-test-mapstruct-and-service-class-entity-form.service';
import { FieldTestMapstructAndServiceClassEntityService } from '../service/field-test-mapstruct-and-service-class-entity.service';
import { IFieldTestMapstructAndServiceClassEntity } from '../field-test-mapstruct-and-service-class-entity.model';

import { FieldTestMapstructAndServiceClassEntityUpdate } from './field-test-mapstruct-and-service-class-entity-update';

describe('FieldTestMapstructAndServiceClassEntity Management Update Component', () => {
  let comp: FieldTestMapstructAndServiceClassEntityUpdate;
  let fixture: ComponentFixture<FieldTestMapstructAndServiceClassEntityUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestMapstructAndServiceClassEntityFormService: FieldTestMapstructAndServiceClassEntityFormService;
  let fieldTestMapstructAndServiceClassEntityService: FieldTestMapstructAndServiceClassEntityService;

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

    fixture = TestBed.createComponent(FieldTestMapstructAndServiceClassEntityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestMapstructAndServiceClassEntityFormService = TestBed.inject(FieldTestMapstructAndServiceClassEntityFormService);
    fieldTestMapstructAndServiceClassEntityService = TestBed.inject(FieldTestMapstructAndServiceClassEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestMapstructAndServiceClassEntity: IFieldTestMapstructAndServiceClassEntity = { id: 340 };

      activatedRoute.data = of({ fieldTestMapstructAndServiceClassEntity });
      comp.ngOnInit();

      expect(comp.fieldTestMapstructAndServiceClassEntity).toEqual(fieldTestMapstructAndServiceClassEntity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestMapstructAndServiceClassEntity>();
      const fieldTestMapstructAndServiceClassEntity = { id: 473 };
      vi.spyOn(fieldTestMapstructAndServiceClassEntityFormService, 'getFieldTestMapstructAndServiceClassEntity').mockReturnValue(
        fieldTestMapstructAndServiceClassEntity,
      );
      vi.spyOn(fieldTestMapstructAndServiceClassEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestMapstructAndServiceClassEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestMapstructAndServiceClassEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestMapstructAndServiceClassEntityFormService.getFieldTestMapstructAndServiceClassEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestMapstructAndServiceClassEntityService.update).toHaveBeenCalledWith(
        expect.objectContaining(fieldTestMapstructAndServiceClassEntity),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestMapstructAndServiceClassEntity>();
      const fieldTestMapstructAndServiceClassEntity = { id: 473 };
      vi.spyOn(fieldTestMapstructAndServiceClassEntityFormService, 'getFieldTestMapstructAndServiceClassEntity').mockReturnValue({
        id: null,
      });
      vi.spyOn(fieldTestMapstructAndServiceClassEntityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestMapstructAndServiceClassEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestMapstructAndServiceClassEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestMapstructAndServiceClassEntityFormService.getFieldTestMapstructAndServiceClassEntity).toHaveBeenCalled();
      expect(fieldTestMapstructAndServiceClassEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestMapstructAndServiceClassEntity>();
      const fieldTestMapstructAndServiceClassEntity = { id: 473 };
      vi.spyOn(fieldTestMapstructAndServiceClassEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestMapstructAndServiceClassEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestMapstructAndServiceClassEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
