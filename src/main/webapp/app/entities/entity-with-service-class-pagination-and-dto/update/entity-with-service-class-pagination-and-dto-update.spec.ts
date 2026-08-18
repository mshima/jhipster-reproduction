import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithServiceClassPaginationAndDTOFormService } from './entity-with-service-class-pagination-and-dto-form.service';
import { EntityWithServiceClassPaginationAndDTOService } from '../service/entity-with-service-class-pagination-and-dto.service';
import { IEntityWithServiceClassPaginationAndDTO } from '../entity-with-service-class-pagination-and-dto.model';

import { EntityWithServiceClassPaginationAndDTOUpdate } from './entity-with-service-class-pagination-and-dto-update';

describe('EntityWithServiceClassPaginationAndDTO Management Update Component', () => {
  let comp: EntityWithServiceClassPaginationAndDTOUpdate;
  let fixture: ComponentFixture<EntityWithServiceClassPaginationAndDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithServiceClassPaginationAndDTOFormService: EntityWithServiceClassPaginationAndDTOFormService;
  let entityWithServiceClassPaginationAndDTOService: EntityWithServiceClassPaginationAndDTOService;

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

    fixture = TestBed.createComponent(EntityWithServiceClassPaginationAndDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithServiceClassPaginationAndDTOFormService = TestBed.inject(EntityWithServiceClassPaginationAndDTOFormService);
    entityWithServiceClassPaginationAndDTOService = TestBed.inject(EntityWithServiceClassPaginationAndDTOService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithServiceClassPaginationAndDTO: IEntityWithServiceClassPaginationAndDTO = { id: 2653 };

      activatedRoute.data = of({ entityWithServiceClassPaginationAndDTO });
      comp.ngOnInit();

      expect(comp.entityWithServiceClassPaginationAndDTO).toEqual(entityWithServiceClassPaginationAndDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassPaginationAndDTO>();
      const entityWithServiceClassPaginationAndDTO = { id: 484 };
      vi.spyOn(entityWithServiceClassPaginationAndDTOFormService, 'getEntityWithServiceClassPaginationAndDTO').mockReturnValue(
        entityWithServiceClassPaginationAndDTO,
      );
      vi.spyOn(entityWithServiceClassPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceClassPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceClassPaginationAndDTOFormService.getEntityWithServiceClassPaginationAndDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithServiceClassPaginationAndDTOService.update).toHaveBeenCalledWith(
        expect.objectContaining(entityWithServiceClassPaginationAndDTO),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassPaginationAndDTO>();
      const entityWithServiceClassPaginationAndDTO = { id: 484 };
      vi.spyOn(entityWithServiceClassPaginationAndDTOFormService, 'getEntityWithServiceClassPaginationAndDTO').mockReturnValue({
        id: null,
      });
      vi.spyOn(entityWithServiceClassPaginationAndDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassPaginationAndDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceClassPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceClassPaginationAndDTOFormService.getEntityWithServiceClassPaginationAndDTO).toHaveBeenCalled();
      expect(entityWithServiceClassPaginationAndDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceClassPaginationAndDTO>();
      const entityWithServiceClassPaginationAndDTO = { id: 484 };
      vi.spyOn(entityWithServiceClassPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceClassPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithServiceClassPaginationAndDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
