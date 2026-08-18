import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithServiceImplAndDTOFormService } from './entity-with-service-impl-and-dto-form.service';
import { EntityWithServiceImplAndDTOService } from '../service/entity-with-service-impl-and-dto.service';
import { IEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';

import { EntityWithServiceImplAndDTOUpdate } from './entity-with-service-impl-and-dto-update';

describe('EntityWithServiceImplAndDTO Management Update Component', () => {
  let comp: EntityWithServiceImplAndDTOUpdate;
  let fixture: ComponentFixture<EntityWithServiceImplAndDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithServiceImplAndDTOFormService: EntityWithServiceImplAndDTOFormService;
  let entityWithServiceImplAndDTOService: EntityWithServiceImplAndDTOService;

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

    fixture = TestBed.createComponent(EntityWithServiceImplAndDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithServiceImplAndDTOFormService = TestBed.inject(EntityWithServiceImplAndDTOFormService);
    entityWithServiceImplAndDTOService = TestBed.inject(EntityWithServiceImplAndDTOService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO = { id: 772 };

      activatedRoute.data = of({ entityWithServiceImplAndDTO });
      comp.ngOnInit();

      expect(comp.entityWithServiceImplAndDTO).toEqual(entityWithServiceImplAndDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndDTO>();
      const entityWithServiceImplAndDTO = { id: 30188 };
      vi.spyOn(entityWithServiceImplAndDTOFormService, 'getEntityWithServiceImplAndDTO').mockReturnValue(entityWithServiceImplAndDTO);
      vi.spyOn(entityWithServiceImplAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplAndDTOFormService.getEntityWithServiceImplAndDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithServiceImplAndDTOService.update).toHaveBeenCalledWith(expect.objectContaining(entityWithServiceImplAndDTO));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndDTO>();
      const entityWithServiceImplAndDTO = { id: 30188 };
      vi.spyOn(entityWithServiceImplAndDTOFormService, 'getEntityWithServiceImplAndDTO').mockReturnValue({ id: null });
      vi.spyOn(entityWithServiceImplAndDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithServiceImplAndDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithServiceImplAndDTOFormService.getEntityWithServiceImplAndDTO).toHaveBeenCalled();
      expect(entityWithServiceImplAndDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithServiceImplAndDTO>();
      const entityWithServiceImplAndDTO = { id: 30188 };
      vi.spyOn(entityWithServiceImplAndDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithServiceImplAndDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithServiceImplAndDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
