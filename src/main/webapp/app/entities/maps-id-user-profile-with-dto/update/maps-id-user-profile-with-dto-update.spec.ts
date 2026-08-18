import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject, from } from 'rxjs';

import { MapsIdUserProfileWithDTOFormService } from './maps-id-user-profile-with-dto-form.service';
import { MapsIdUserProfileWithDTOService } from '../service/maps-id-user-profile-with-dto.service';
import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';

import { MapsIdUserProfileWithDTOUpdate } from './maps-id-user-profile-with-dto-update';

describe('MapsIdUserProfileWithDTO Management Update Component', () => {
  let comp: MapsIdUserProfileWithDTOUpdate;
  let fixture: ComponentFixture<MapsIdUserProfileWithDTOUpdate>;
  let activatedRoute: ActivatedRoute;
  let mapsIdUserProfileWithDTOFormService: MapsIdUserProfileWithDTOFormService;
  let mapsIdUserProfileWithDTOService: MapsIdUserProfileWithDTOService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(MapsIdUserProfileWithDTOUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mapsIdUserProfileWithDTOFormService = TestBed.inject(MapsIdUserProfileWithDTOFormService);
    mapsIdUserProfileWithDTOService = TestBed.inject(MapsIdUserProfileWithDTOService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = { id: 13667 };
      const user: IUser = { id: 3944 };
      mapsIdUserProfileWithDTO.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mapsIdUserProfileWithDTO });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO = { id: 13667 };
      const user: IUser = { id: 3944 };
      mapsIdUserProfileWithDTO.user = user;

      activatedRoute.data = of({ mapsIdUserProfileWithDTO });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(user);
      expect(comp.mapsIdUserProfileWithDTO).toEqual(mapsIdUserProfileWithDTO);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMapsIdUserProfileWithDTO>();
      const mapsIdUserProfileWithDTO = { id: 8289 };
      vi.spyOn(mapsIdUserProfileWithDTOFormService, 'getMapsIdUserProfileWithDTO').mockReturnValue(mapsIdUserProfileWithDTO);
      vi.spyOn(mapsIdUserProfileWithDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mapsIdUserProfileWithDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mapsIdUserProfileWithDTO);
      saveSubject.complete();

      // THEN
      expect(mapsIdUserProfileWithDTOFormService.getMapsIdUserProfileWithDTO).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mapsIdUserProfileWithDTOService.update).toHaveBeenCalledWith(expect.objectContaining(mapsIdUserProfileWithDTO));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMapsIdUserProfileWithDTO>();
      const mapsIdUserProfileWithDTO = { id: 8289 };
      vi.spyOn(mapsIdUserProfileWithDTOFormService, 'getMapsIdUserProfileWithDTO').mockReturnValue({ id: null });
      vi.spyOn(mapsIdUserProfileWithDTOService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mapsIdUserProfileWithDTO: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mapsIdUserProfileWithDTO);
      saveSubject.complete();

      // THEN
      expect(mapsIdUserProfileWithDTOFormService.getMapsIdUserProfileWithDTO).toHaveBeenCalled();
      expect(mapsIdUserProfileWithDTOService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMapsIdUserProfileWithDTO>();
      const mapsIdUserProfileWithDTO = { id: 8289 };
      vi.spyOn(mapsIdUserProfileWithDTOService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mapsIdUserProfileWithDTO });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mapsIdUserProfileWithDTOService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
