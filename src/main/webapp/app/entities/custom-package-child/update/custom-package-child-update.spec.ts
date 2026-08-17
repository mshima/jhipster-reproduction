import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustomPackageParent } from 'app/entities/custom-package-parent/custom-package-parent.model';
import { CustomPackageParentService } from 'app/entities/custom-package-parent/service/custom-package-parent.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { ICustomPackageChild } from '../custom-package-child.model';
import { CustomPackageChildService } from '../service/custom-package-child.service';

import { CustomPackageChildFormService } from './custom-package-child-form.service';
import { CustomPackageChildUpdate } from './custom-package-child-update';

describe('CustomPackageChild Management Update Component', () => {
  let comp: CustomPackageChildUpdate;
  let fixture: ComponentFixture<CustomPackageChildUpdate>;
  let activatedRoute: ActivatedRoute;
  let customPackageChildFormService: CustomPackageChildFormService;
  let customPackageChildService: CustomPackageChildService;
  let userService: UserService;
  let customPackageParentService: CustomPackageParentService;

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

    fixture = TestBed.createComponent(CustomPackageChildUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customPackageChildFormService = TestBed.inject(CustomPackageChildFormService);
    customPackageChildService = TestBed.inject(CustomPackageChildService);
    userService = TestBed.inject(UserService);
    customPackageParentService = TestBed.inject(CustomPackageParentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const customPackageChild: ICustomPackageChild = { id: 30280 };
      const user: IUser = { id: 3944 };
      customPackageChild.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customPackageChild });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustomPackageParent query and add missing value', () => {
      const customPackageChild: ICustomPackageChild = { id: 30280 };
      const customPackageParent: ICustomPackageParent = { id: 26792 };
      customPackageChild.customPackageParent = customPackageParent;

      const customPackageParentCollection: ICustomPackageParent[] = [{ id: 26792 }];
      vi.spyOn(customPackageParentService, 'query').mockReturnValue(of(new HttpResponse({ body: customPackageParentCollection })));
      const additionalCustomPackageParents = [customPackageParent];
      const expectedCollection: ICustomPackageParent[] = [...additionalCustomPackageParents, ...customPackageParentCollection];
      vi.spyOn(customPackageParentService, 'addCustomPackageParentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customPackageChild });
      comp.ngOnInit();

      expect(customPackageParentService.query).toHaveBeenCalled();
      expect(customPackageParentService.addCustomPackageParentToCollectionIfMissing).toHaveBeenCalledWith(
        customPackageParentCollection,
        ...additionalCustomPackageParents.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.customPackageParentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const customPackageChild: ICustomPackageChild = { id: 30280 };
      const user: IUser = { id: 3944 };
      customPackageChild.user = user;
      const customPackageParent: ICustomPackageParent = { id: 26792 };
      customPackageChild.customPackageParent = customPackageParent;

      activatedRoute.data = of({ customPackageChild });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(user);
      expect(comp.customPackageParentsSharedCollection()).toContainEqual(customPackageParent);
      expect(comp.customPackageChild).toEqual(customPackageChild);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageChild>();
      const customPackageChild = { id: 27070 };
      vi.spyOn(customPackageChildFormService, 'getCustomPackageChild').mockReturnValue(customPackageChild);
      vi.spyOn(customPackageChildService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageChild });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customPackageChild);
      saveSubject.complete();

      // THEN
      expect(customPackageChildFormService.getCustomPackageChild).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customPackageChildService.update).toHaveBeenCalledWith(expect.objectContaining(customPackageChild));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageChild>();
      const customPackageChild = { id: 27070 };
      vi.spyOn(customPackageChildFormService, 'getCustomPackageChild').mockReturnValue({ id: null });
      vi.spyOn(customPackageChildService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageChild: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customPackageChild);
      saveSubject.complete();

      // THEN
      expect(customPackageChildFormService.getCustomPackageChild).toHaveBeenCalled();
      expect(customPackageChildService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomPackageChild>();
      const customPackageChild = { id: 27070 };
      vi.spyOn(customPackageChildService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customPackageChild });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customPackageChildService.update).toHaveBeenCalled();
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

    describe('compareCustomPackageParent', () => {
      it('should forward to customPackageParentService', () => {
        const entity = { id: 26792 };
        const entity2 = { id: 3080 };
        vi.spyOn(customPackageParentService, 'compareCustomPackageParent');
        comp.compareCustomPackageParent(entity, entity2);
        expect(customPackageParentService.compareCustomPackageParent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
