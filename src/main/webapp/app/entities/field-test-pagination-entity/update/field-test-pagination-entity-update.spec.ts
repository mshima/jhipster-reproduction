import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestPaginationEntityFormService } from './field-test-pagination-entity-form.service';
import { FieldTestPaginationEntityService } from '../service/field-test-pagination-entity.service';
import { IFieldTestPaginationEntity } from '../field-test-pagination-entity.model';

import { FieldTestPaginationEntityUpdate } from './field-test-pagination-entity-update';

describe('FieldTestPaginationEntity Management Update Component', () => {
  let comp: FieldTestPaginationEntityUpdate;
  let fixture: ComponentFixture<FieldTestPaginationEntityUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestPaginationEntityFormService: FieldTestPaginationEntityFormService;
  let fieldTestPaginationEntityService: FieldTestPaginationEntityService;

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

    fixture = TestBed.createComponent(FieldTestPaginationEntityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestPaginationEntityFormService = TestBed.inject(FieldTestPaginationEntityFormService);
    fieldTestPaginationEntityService = TestBed.inject(FieldTestPaginationEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestPaginationEntity: IFieldTestPaginationEntity = { id: 2959 };

      activatedRoute.data = of({ fieldTestPaginationEntity });
      comp.ngOnInit();

      expect(comp.fieldTestPaginationEntity).toEqual(fieldTestPaginationEntity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestPaginationEntity>();
      const fieldTestPaginationEntity = { id: 16266 };
      vi.spyOn(fieldTestPaginationEntityFormService, 'getFieldTestPaginationEntity').mockReturnValue(fieldTestPaginationEntity);
      vi.spyOn(fieldTestPaginationEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestPaginationEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestPaginationEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestPaginationEntityFormService.getFieldTestPaginationEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestPaginationEntityService.update).toHaveBeenCalledWith(expect.objectContaining(fieldTestPaginationEntity));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestPaginationEntity>();
      const fieldTestPaginationEntity = { id: 16266 };
      vi.spyOn(fieldTestPaginationEntityFormService, 'getFieldTestPaginationEntity').mockReturnValue({ id: null });
      vi.spyOn(fieldTestPaginationEntityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestPaginationEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestPaginationEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestPaginationEntityFormService.getFieldTestPaginationEntity).toHaveBeenCalled();
      expect(fieldTestPaginationEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestPaginationEntity>();
      const fieldTestPaginationEntity = { id: 16266 };
      vi.spyOn(fieldTestPaginationEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestPaginationEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestPaginationEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
