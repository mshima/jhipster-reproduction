import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithPaginationAndDTOFormService } from './entity-with-pagination-and-dto-form.service';
import { EntityWithPaginationAndDTOService } from '../service/entity-with-pagination-and-dto.service';
import { IEntityWithPaginationAndDTO } from '../entity-with-pagination-and-dto.model';

import { EntityWithPaginationAndDTOUpdate } from './entity-with-pagination-and-dto-update';

describe('EntityWithPaginationAndDTO Management Update Component', () => {
  let comp: EntityWithPaginationAndDTOUpdate;
  let fixture: ComponentFixture<EntityWithPaginationAndDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithPaginationAndDTOFormService: EntityWithPaginationAndDTOFormService;
  let entityWithPaginationAndDTOService: EntityWithPaginationAndDTOService;

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

    fixture = TestBed.createComponent(EntityWithPaginationAndDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithPaginationAndDTOFormService = TestBed.inject(EntityWithPaginationAndDTOFormService);
    entityWithPaginationAndDTOService = TestBed.inject(EntityWithPaginationAndDTOService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithPaginationAndDTO: IEntityWithPaginationAndDTO = { id: 22670 };

      activatedRoute.data = of({ entityWithPaginationAndDTO });
      comp.ngOnInit();

      expect(comp.entityWithPaginationAndDTO).toEqual(entityWithPaginationAndDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithPaginationAndDTO>();
      const entityWithPaginationAndDTO = { id: 26975 };
      vi.spyOn(entityWithPaginationAndDTOFormService, 'getEntityWithPaginationAndDTO').mockReturnValue(entityWithPaginationAndDTO);
      vi.spyOn(entityWithPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithPaginationAndDTOFormService.getEntityWithPaginationAndDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithPaginationAndDTOService.update).toHaveBeenCalledWith(expect.objectContaining(entityWithPaginationAndDTO));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithPaginationAndDTO>();
      const entityWithPaginationAndDTO = { id: 26975 };
      vi.spyOn(entityWithPaginationAndDTOFormService, 'getEntityWithPaginationAndDTO').mockReturnValue({ id: null });
      vi.spyOn(entityWithPaginationAndDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithPaginationAndDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithPaginationAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithPaginationAndDTOFormService.getEntityWithPaginationAndDTO).toHaveBeenCalled();
      expect(entityWithPaginationAndDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithPaginationAndDTO>();
      const entityWithPaginationAndDTO = { id: 26975 };
      vi.spyOn(entityWithPaginationAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithPaginationAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithPaginationAndDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
