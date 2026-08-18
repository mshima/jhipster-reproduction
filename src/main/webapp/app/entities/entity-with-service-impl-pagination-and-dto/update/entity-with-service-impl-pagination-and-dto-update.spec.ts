import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithServiceImplPaginationAndDTOFormService } from './entity-with-service-impl-pagination-and-dto-form.service';
import { EntityWithServiceImplPaginationAndDTOService } from '../service/entity-with-service-impl-pagination-and-dto.service';
import { IEntityWithServiceImplPaginationAndDTO } from '../entity-with-service-impl-pagination-and-dto.model';

import { EntityWithServiceImplPaginationAndDTOUpdate } from './entity-with-service-impl-pagination-and-dto-update';

describe('EntityWithServiceImplPaginationAndDTO Management Update Component', () => {
  let comp: EntityWithServiceImplPaginationAndDTOUpdate;
  let fixture: ComponentFixture<EntityWithServiceImplPaginationAndDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithServiceImplPaginationAndDTOFormService: EntityWithServiceImplPaginationAndDTOFormService;
  let entityWithServiceImplPaginationAndDTOService: EntityWithServiceImplPaginationAndDTOService;

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

    fixture = TestBed.createComponent(EntityWithServiceImplPaginationAndDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithServiceImplPaginationAndDTOFormService = TestBed.inject(EntityWithServiceImplPaginationAndDTOFormService);
    entityWithServiceImplPaginationAndDTOService = TestBed.inject(EntityWithServiceImplPaginationAndDTOService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO = { id: 11915 };

      activatedRoute.data = of({ entityWithServiceImplPaginationAndDTO });
      comp.ngOnInit();

      expect(comp.entityWithServiceImplPaginationAndDTO).toEqual(entityWithServiceImplPaginationAndDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplPaginationAndDTO>();
      const entityWithServiceImplPaginationAndDTO = { id: 19834 };
      vi.spyOn(entityWithServiceImplPaginationAndDTOFormService, 'getEntityWithServiceImplPaginationAndDTO').mockReturnValue(
        entityWithServiceImplPaginationAndDTO,
      );
      vi.spyOn(entityWithServiceImplPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplPaginationAndDTOFormService.getEntityWithServiceImplPaginationAndDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithServiceImplPaginationAndDTOService.update).toHaveBeenCalledWith(
        expect.objectContaining(entityWithServiceImplPaginationAndDTO),
      );
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplPaginationAndDTO>();
      const entityWithServiceImplPaginationAndDTO = { id: 19834 };
      vi.spyOn(entityWithServiceImplPaginationAndDTOFormService, 'getEntityWithServiceImplPaginationAndDTO').mockReturnValue({ id: null });
      vi.spyOn(entityWithServiceImplPaginationAndDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplPaginationAndDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplPaginationAndDTOFormService.getEntityWithServiceImplPaginationAndDTO).toHaveBeenCalled();
      expect(entityWithServiceImplPaginationAndDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplPaginationAndDTO>();
      const entityWithServiceImplPaginationAndDTO = { id: 19834 };
      vi.spyOn(entityWithServiceImplPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithServiceImplPaginationAndDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
