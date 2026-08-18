import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import {
  EntityWithServiceImplAndDTOFormService,
  EntityWithServiceImplAndDTOFormGroup,
} from './entity-with-service-impl-and-dto-form.service';
import { IEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';
import { EntityWithServiceImplAndDTOService } from '../service/entity-with-service-impl-and-dto.service';

@Component({
  selector: 'jhi-entity-with-service-impl-and-dto-update',
  templateUrl: './entity-with-service-impl-and-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithServiceImplAndDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO | null = null;

  protected entityWithServiceImplAndDTOService = inject(EntityWithServiceImplAndDTOService);
  protected entityWithServiceImplAndDTOFormService = inject(EntityWithServiceImplAndDTOFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithServiceImplAndDTOFormGroup = this.entityWithServiceImplAndDTOFormService.createEntityWithServiceImplAndDTOFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithServiceImplAndDTO }) => {
      this.entityWithServiceImplAndDTO = entityWithServiceImplAndDTO;
      if (entityWithServiceImplAndDTO) {
        this.updateForm(entityWithServiceImplAndDTO);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithServiceImplAndDTO = this.entityWithServiceImplAndDTOFormService.getEntityWithServiceImplAndDTO(this.editForm);
    if (entityWithServiceImplAndDTO.id === null) {
      this.subscribeToSaveResponse(this.entityWithServiceImplAndDTOService.create(entityWithServiceImplAndDTO));
    } else {
      this.subscribeToSaveResponse(this.entityWithServiceImplAndDTOService.update(entityWithServiceImplAndDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithServiceImplAndDTO | null>): void {
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

  protected updateForm(entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO): void {
    this.entityWithServiceImplAndDTO = entityWithServiceImplAndDTO;
    this.entityWithServiceImplAndDTOFormService.resetForm(this.editForm, entityWithServiceImplAndDTO);
  }
}
