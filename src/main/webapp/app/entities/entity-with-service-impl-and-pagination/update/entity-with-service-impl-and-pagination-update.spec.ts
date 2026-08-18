import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithServiceImplAndPaginationFormService } from './entity-with-service-impl-and-pagination-form.service';
import { EntityWithServiceImplAndPaginationService } from '../service/entity-with-service-impl-and-pagination.service';
import { IEntityWithServiceImplAndPagination } from '../entity-with-service-impl-and-pagination.model';

import { EntityWithServiceImplAndPaginationUpdate } from './entity-with-service-impl-and-pagination-update';

describe('EntityWithServiceImplAndPagination Management Update Component', () => {
  let comp: EntityWithServiceImplAndPaginationUpdate;
  let fixture: ComponentFixture<EntityWithServiceImplAndPaginationUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithServiceImplAndPaginationFormService: EntityWithServiceImplAndPaginationFormService;
  let entityWithServiceImplAndPaginationService: EntityWithServiceImplAndPaginationService;

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

    fixture = TestBed.createComponent(EntityWithServiceImplAndPaginationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithServiceImplAndPaginationFormService = TestBed.inject(EntityWithServiceImplAndPaginationFormService);
    entityWithServiceImplAndPaginationService = TestBed.inject(EntityWithServiceImplAndPaginationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination = { id: 3861 };

      activatedRoute.data = of({ entityWithServiceImplAndPagination });
      comp.ngOnInit();

      expect(comp.entityWithServiceImplAndPagination).toEqual(entityWithServiceImplAndPagination);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndPagination>();
      const entityWithServiceImplAndPagination = { id: 27409 };
      vi.spyOn(entityWithServiceImplAndPaginationFormService, 'getEntityWithServiceImplAndPagination').mockReturnValue(
        entityWithServiceImplAndPagination,
      );
      vi.spyOn(entityWithServiceImplAndPaginationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndPagination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplAndPagination);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplAndPaginationFormService.getEntityWithServiceImplAndPagination).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithServiceImplAndPaginationService.update).toHaveBeenCalledWith(
        expect.objectContaining(entityWithServiceImplAndPagination),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndPagination>();
      const entityWithServiceImplAndPagination = { id: 27409 };
      vi.spyOn(entityWithServiceImplAndPaginationFormService, 'getEntityWithServiceImplAndPagination').mockReturnValue({ id: null });
      vi.spyOn(entityWithServiceImplAndPaginationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndPagination: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplAndPagination);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplAndPaginationFormService.getEntityWithServiceImplAndPagination).toHaveBeenCalled();
      expect(entityWithServiceImplAndPaginationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndPagination>();
      const entityWithServiceImplAndPagination = { id: 27409 };
      vi.spyOn(entityWithServiceImplAndPaginationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndPagination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithServiceImplAndPaginationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
