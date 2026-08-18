import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { FieldTestServiceClassAndJpaFilteringEntityFormService } from './field-test-service-class-and-jpa-filtering-entity-form.service';
import { FieldTestServiceClassAndJpaFilteringEntityService } from '../service/field-test-service-class-and-jpa-filtering-entity.service';
import { IFieldTestServiceClassAndJpaFilteringEntity } from '../field-test-service-class-and-jpa-filtering-entity.model';

import { FieldTestServiceClassAndJpaFilteringEntityUpdate } from './field-test-service-class-and-jpa-filtering-entity-update';

describe('FieldTestServiceClassAndJpaFilteringEntity Management Update Component', () => {
  let comp: FieldTestServiceClassAndJpaFilteringEntityUpdate;
  let fixture: ComponentFixture<FieldTestServiceClassAndJpaFilteringEntityUpdate>;
  let activatedRoute: ActivatedRoute;
  let fieldTestServiceClassAndJpaFilteringEntityFormService: FieldTestServiceClassAndJpaFilteringEntityFormService;
  let fieldTestServiceClassAndJpaFilteringEntityService: FieldTestServiceClassAndJpaFilteringEntityService;

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

    fixture = TestBed.createComponent(FieldTestServiceClassAndJpaFilteringEntityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fieldTestServiceClassAndJpaFilteringEntityFormService = TestBed.inject(FieldTestServiceClassAndJpaFilteringEntityFormService);
    fieldTestServiceClassAndJpaFilteringEntityService = TestBed.inject(FieldTestServiceClassAndJpaFilteringEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fieldTestServiceClassAndJpaFilteringEntity: IFieldTestServiceClassAndJpaFilteringEntity = { id: 1083 };

      activatedRoute.data = of({ fieldTestServiceClassAndJpaFilteringEntity });
      comp.ngOnInit();

      expect(comp.fieldTestServiceClassAndJpaFilteringEntity).toEqual(fieldTestServiceClassAndJpaFilteringEntity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceClassAndJpaFilteringEntity>();
      const fieldTestServiceClassAndJpaFilteringEntity = { id: 26563 };
      vi.spyOn(fieldTestServiceClassAndJpaFilteringEntityFormService, 'getFieldTestServiceClassAndJpaFilteringEntity').mockReturnValue(
        fieldTestServiceClassAndJpaFilteringEntity,
      );
      vi.spyOn(fieldTestServiceClassAndJpaFilteringEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceClassAndJpaFilteringEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestServiceClassAndJpaFilteringEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestServiceClassAndJpaFilteringEntityFormService.getFieldTestServiceClassAndJpaFilteringEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fieldTestServiceClassAndJpaFilteringEntityService.update).toHaveBeenCalledWith(
        expect.objectContaining(fieldTestServiceClassAndJpaFilteringEntity),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceClassAndJpaFilteringEntity>();
      const fieldTestServiceClassAndJpaFilteringEntity = { id: 26563 };
      vi.spyOn(fieldTestServiceClassAndJpaFilteringEntityFormService, 'getFieldTestServiceClassAndJpaFilteringEntity').mockReturnValue({
        id: null,
      });
      vi.spyOn(fieldTestServiceClassAndJpaFilteringEntityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceClassAndJpaFilteringEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fieldTestServiceClassAndJpaFilteringEntity);
      saveSubject.complete();

      // THEN
      expect(fieldTestServiceClassAndJpaFilteringEntityFormService.getFieldTestServiceClassAndJpaFilteringEntity).toHaveBeenCalled();
      expect(fieldTestServiceClassAndJpaFilteringEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFieldTestServiceClassAndJpaFilteringEntity>();
      const fieldTestServiceClassAndJpaFilteringEntity = { id: 26563 };
      vi.spyOn(fieldTestServiceClassAndJpaFilteringEntityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fieldTestServiceClassAndJpaFilteringEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fieldTestServiceClassAndJpaFilteringEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
