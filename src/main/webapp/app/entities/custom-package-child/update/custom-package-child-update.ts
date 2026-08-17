import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustomPackageParent } from 'app/entities/custom-package-parent/custom-package-parent.model';
import { CustomPackageParentService } from 'app/entities/custom-package-parent/service/custom-package-parent.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICustomPackageChild } from '../custom-package-child.model';
import { CustomPackageChildService } from '../service/custom-package-child.service';

import { CustomPackageChildFormGroup, CustomPackageChildFormService } from './custom-package-child-form.service';

@Component({
  selector: 'jhi-custom-package-child-update',
  templateUrl: './custom-package-child-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustomPackageChildUpdate implements OnInit {
  readonly isSaving = signal(false);
  customPackageChild: ICustomPackageChild | null = null;

  usersSharedCollection = signal<IUser[]>([]);
  customPackageParentsSharedCollection = signal<ICustomPackageParent[]>([]);

  protected customPackageChildService = inject(CustomPackageChildService);
  protected customPackageChildFormService = inject(CustomPackageChildFormService);
  protected userService = inject(UserService);
  protected customPackageParentService = inject(CustomPackageParentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomPackageChildFormGroup = this.customPackageChildFormService.createCustomPackageChildFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareCustomPackageParent = (o1: ICustomPackageParent | null, o2: ICustomPackageParent | null): boolean =>
    this.customPackageParentService.compareCustomPackageParent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customPackageChild }) => {
      this.customPackageChild = customPackageChild;
      if (customPackageChild) {
        this.updateForm(customPackageChild);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const customPackageChild = this.customPackageChildFormService.getCustomPackageChild(this.editForm);
    if (customPackageChild.id === null) {
      this.subscribeToSaveResponse(this.customPackageChildService.create(customPackageChild));
    } else {
      this.subscribeToSaveResponse(this.customPackageChildService.update(customPackageChild));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustomPackageChild | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(customPackageChild: ICustomPackageChild): void {
    this.customPackageChild = customPackageChild;
    this.customPackageChildFormService.resetForm(this.editForm, customPackageChild);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, customPackageChild.user));
    this.customPackageParentsSharedCollection.update(customPackageParents =>
      this.customPackageParentService.addCustomPackageParentToCollectionIfMissing<ICustomPackageParent>(
        customPackageParents,
        customPackageChild.customPackageParent,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.customPackageChild?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.customPackageParentService
      .query()
      .pipe(map((res: HttpResponse<ICustomPackageParent[]>) => res.body ?? []))
      .pipe(
        map((customPackageParents: ICustomPackageParent[]) =>
          this.customPackageParentService.addCustomPackageParentToCollectionIfMissing<ICustomPackageParent>(
            customPackageParents,
            this.customPackageChild?.customPackageParent,
          ),
        ),
      )
      .subscribe((customPackageParents: ICustomPackageParent[]) => this.customPackageParentsSharedCollection.set(customPackageParents));
  }
}
