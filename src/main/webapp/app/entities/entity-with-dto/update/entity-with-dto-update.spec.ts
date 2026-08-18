import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { EntityWithDTOFormService } from './entity-with-dto-form.service';
import { EntityWithDTOService } from '../service/entity-with-dto.service';
import { IEntityWithDTO } from '../entity-with-dto.model';

import { EntityWithDTOUpdate } from './entity-with-dto-update';

describe('EntityWithDTO Management Update Component', () => {
  let comp: EntityWithDTOUpdate;
  let fixture: ComponentFixture<EntityWithDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let entityWithDTOFormService: EntityWithDTOFormService;
  let entityWithDTOService: EntityWithDTOService;

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

    fixture = TestBed.createComponent(EntityWithDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    entityWithDTOFormService = TestBed.inject(EntityWithDTOFormService);
    entityWithDTOService = TestBed.inject(EntityWithDTOService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const entityWithDTO: IEntityWithDTO = { id: 29009 };

      activatedRoute.data = of({ entityWithDTO });
      comp.ngOnInit();

      expect(comp.entityWithDTO).toEqual(entityWithDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithDTO>();
      const entityWithDTO = { id: 13981 };
      vi.spyOn(entityWithDTOFormService, 'getEntityWithDTO').mockReturnValue(entityWithDTO);
      vi.spyOn(entityWithDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithDTOFormService.getEntityWithDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(entityWithDTOService.update).toHaveBeenCalledWith(expect.objectContaining(entityWithDTO));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithDTO>();
      const entityWithDTO = { id: 13981 };
      vi.spyOn(entityWithDTOFormService, 'getEntityWithDTO').mockReturnValue({ id: null });
      vi.spyOn(entityWithDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(entityWithDTO);
      saveSubject.complete();

      // THEN
      expect(entityWithDTOFormService.getEntityWithDTO).toHaveBeenCalled();
      expect(entityWithDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEntityWithDTO>();
      const entityWithDTO = { id: 13981 };
      vi.spyOn(entityWithDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ entityWithDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(entityWithDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
