import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, map, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { MapsIdUserProfileWithDTOFormService, MapsIdUserProfileWithDTOFormGroup } from './maps-id-user-profile-with-dto-form.service';
import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';
import { MapsIdUserProfileWithDTOService } from '../service/maps-id-user-profile-with-dto.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';

@Component({
  selector: 'jhi-maps-id-user-profile-with-dto-update',
  templateUrl: './maps-id-user-profile-with-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class MapsIdUserProfileWithDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO | null = null;

  usersSharedCollection = signal<IUser[]>([]);

  protected mapsIdUserProfileWithDTOService = inject(MapsIdUserProfileWithDTOService);
  protected mapsIdUserProfileWithDTOFormService = inject(MapsIdUserProfileWithDTOFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MapsIdUserProfileWithDTOFormGroup = this.mapsIdUserProfileWithDTOFormService.createMapsIdUserProfileWithDTOFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mapsIdUserProfileWithDTO }) => {
      this.mapsIdUserProfileWithDTO = mapsIdUserProfileWithDTO;
      if (mapsIdUserProfileWithDTO) {
        this.updateForm(mapsIdUserProfileWithDTO);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const mapsIdUserProfileWithDTO = this.mapsIdUserProfileWithDTOFormService.getMapsIdUserProfileWithDTO(this.editForm);
    if (mapsIdUserProfileWithDTO.id === null) {
      this.subscribeToSaveResponse(this.mapsIdUserProfileWithDTOService.create(mapsIdUserProfileWithDTO));
    } else {
      this.subscribeToSaveResponse(this.mapsIdUserProfileWithDTOService.update(mapsIdUserProfileWithDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMapsIdUserProfileWithDTO | null>): void {
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

  protected updateForm(mapsIdUserProfileWithDTO: IMapsIdUserProfileWithDTO): void {
    this.mapsIdUserProfileWithDTO = mapsIdUserProfileWithDTO;
    this.mapsIdUserProfileWithDTOFormService.resetForm(this.editForm, mapsIdUserProfileWithDTO);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, mapsIdUserProfileWithDTO.user));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.mapsIdUserProfileWithDTO?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
