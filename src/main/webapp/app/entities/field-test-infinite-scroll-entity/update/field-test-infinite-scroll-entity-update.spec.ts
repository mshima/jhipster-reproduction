import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestInfiniteScrollEntityFormService } from './field-test-infinite-scroll-entity-form.service';
import { FieldTestInfiniteScrollEntityService } from '../service/field-test-infinite-scroll-entity.service';
import { IFieldTestInfiniteScrollEntity } from '../field-test-infinite-scroll-entity.model';

import { FieldTestInfiniteScrollEntityUpdate } from './field-test-infinite-scroll-entity-update';

describe('FieldTestInfiniteScrollEntity Management Update Component', () => {
  let comp: FieldTestInfiniteScrollEntityUpdate;
  let fixture: ComponentFixture<FieldTestInfiniteScrollEntityUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestInfiniteScrollEntityFormService: FieldTestInfiniteScrollEntityFormService;
  let fieldTestInfiniteScrollEntityService: FieldTestInfiniteScrollEntityService;

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

    fixture = TestBed.createComponent(FieldTestInfiniteScrollEntityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestInfiniteScrollEntityFormService = TestBed.inject(FieldTestInfiniteScrollEntityFormService);
    fieldTestInfiniteScrollEntityService = TestBed.inject(FieldTestInfiniteScrollEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity = { id: 12964 };

      activatedRoute.data = of({ fieldTestInfiniteScrollEntity });
      comp.ngOnInit();

      expect(comp.fieldTestInfiniteScrollEntity).toEqual(fieldTestInfiniteScrollEntity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestInfiniteScrollEntity>();
      const fieldTestInfiniteScrollEntity = { id: 20874 };
      vi.spyOn(fieldTestInfiniteScrollEntityFormService, 'getFieldTestInfiniteScrollEntity').mockReturnValue(fieldTestInfiniteScrollEntity);
      vi.spyOn(fieldTestInfiniteScrollEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestInfiniteScrollEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestInfiniteScrollEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestInfiniteScrollEntityFormService.getFieldTestInfiniteScrollEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestInfiniteScrollEntityService.update).toHaveBeenCalledWith(expect.objectContaining(fieldTestInfiniteScrollEntity));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestInfiniteScrollEntity>();
      const fieldTestInfiniteScrollEntity = { id: 20874 };
      vi.spyOn(fieldTestInfiniteScrollEntityFormService, 'getFieldTestInfiniteScrollEntity').mockReturnValue({ id: null });
      vi.spyOn(fieldTestInfiniteScrollEntityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestInfiniteScrollEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestInfiniteScrollEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestInfiniteScrollEntityFormService.getFieldTestInfiniteScrollEntity).toHaveBeenCalled();
      expect(fieldTestInfiniteScrollEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestInfiniteScrollEntity>();
      const fieldTestInfiniteScrollEntity = { id: 20874 };
      vi.spyOn(fieldTestInfiniteScrollEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestInfiniteScrollEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestInfiniteScrollEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
