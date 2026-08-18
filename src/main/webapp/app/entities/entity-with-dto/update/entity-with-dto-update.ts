import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { EntityWithDTOFormService, EntityWithDTOFormGroup } from './entity-with-dto-form.service';
import { IEntityWithDTO } from '../entity-with-dto.model';
import { EntityWithDTOService } from '../service/entity-with-dto.service';

@Component({
  selector: 'jhi-entity-with-dto-update',
  templateUrl: './entity-with-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithDTO: IEntityWithDTO | null = null;

  protected entityWithDTOService = inject(EntityWithDTOService);
  protected entityWithDTOFormService = inject(EntityWithDTOFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithDTOFormGroup = this.entityWithDTOFormService.createEntityWithDTOFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithDTO }) => {
      this.entityWithDTO = entityWithDTO;
      if (entityWithDTO) {
        this.updateForm(entityWithDTO);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithDTO = this.entityWithDTOFormService.getEntityWithDTO(this.editForm);
    if (entityWithDTO.id === null) {
      this.subscribeToSaveResponse(this.entityWithDTOService.create(entityWithDTO));
    } else {
      this.subscribeToSaveResponse(this.entityWithDTOService.update(entityWithDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithDTO | null>): void {
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

  protected updateForm(entityWithDTO: IEntityWithDTO): void {
    this.entityWithDTO = entityWithDTO;
    this.entityWithDTOFormService.resetForm(this.editForm, entityWithDTO);
  }
}
