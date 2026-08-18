import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithServiceClassAndPaginationFormService } from './entity-with-service-class-and-pagination-form.service';
import { EntityWithServiceClassAndPaginationService } from '../service/entity-with-service-class-and-pagination.service';
import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';

import { EntityWithServiceClassAndPaginationUpdate } from './entity-with-service-class-and-pagination-update';

describe('EntityWithServiceClassAndPagination Management Update Component', () => {
  let comp: EntityWithServiceClassAndPaginationUpdate;
  let fixture: ComponentFixture<EntityWithServiceClassAndPaginationUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithServiceClassAndPaginationFormService: EntityWithServiceClassAndPaginationFormService;
  let entityWithServiceClassAndPaginationService: EntityWithServiceClassAndPaginationService;

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

    fixture = TestBed.createComponent(EntityWithServiceClassAndPaginationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithServiceClassAndPaginationFormService = TestBed.inject(EntityWithServiceClassAndPaginationFormService);
    entityWithServiceClassAndPaginationService = TestBed.inject(EntityWithServiceClassAndPaginationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination = { id: 16307 };

      activatedRoute.data = of({ entityWithServiceClassAndPagination });
      comp.ngOnInit();

      expect(comp.entityWithServiceClassAndPagination).toEqual(entityWithServiceClassAndPagination);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassAndPagination>();
      const entityWithServiceClassAndPagination = { id: 11699 };
      vi.spyOn(entityWithServiceClassAndPaginationFormService, 'getEntityWithServiceClassAndPagination').mockReturnValue(
        entityWithServiceClassAndPagination,
      );
      vi.spyOn(entityWithServiceClassAndPaginationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassAndPagination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceClassAndPagination);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceClassAndPaginationFormService.getEntityWithServiceClassAndPagination).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithServiceClassAndPaginationService.update).toHaveBeenCalledWith(
        expect.objectContaining(entityWithServiceClassAndPagination),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassAndPagination>();
      const entityWithServiceClassAndPagination = { id: 11699 };
      vi.spyOn(entityWithServiceClassAndPaginationFormService, 'getEntityWithServiceClassAndPagination').mockReturnValue({ id: null });
      vi.spyOn(entityWithServiceClassAndPaginationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassAndPagination: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceClassAndPagination);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceClassAndPaginationFormService.getEntityWithServiceClassAndPagination).toHaveBeenCalled();
      expect(entityWithServiceClassAndPaginationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassAndPagination>();
      const entityWithServiceClassAndPagination = { id: 11699 };
      vi.spyOn(entityWithServiceClassAndPaginationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassAndPagination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithServiceClassAndPaginationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
