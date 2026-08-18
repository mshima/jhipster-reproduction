import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import {
  EntityWithServiceClassPaginationAndDTOFormService,
  EntityWithServiceClassPaginationAndDTOFormGroup,
} from './entity-with-service-class-pagination-and-dto-form.service';
import { IEntityWithServiceClassPaginationAndDTO } from '../entity-with-service-class-pagination-and-dto.model';
import { EntityWithServiceClassPaginationAndDTOService } from '../service/entity-with-service-class-pagination-and-dto.service';

@Component({
  selector: 'jhi-entity-with-service-class-pagination-and-dto-update',
  templateUrl: './entity-with-service-class-pagination-and-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithServiceClassPaginationAndDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithServiceClassPaginationAndDTO: IEntityWithServiceClassPaginationAndDTO | null = null;

  protected entityWithServiceClassPaginationAndDTOService = inject(EntityWithServiceClassPaginationAndDTOService);
  protected entityWithServiceClassPaginationAndDTOFormService = inject(EntityWithServiceClassPaginationAndDTOFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithServiceClassPaginationAndDTOFormGroup =
    this.entityWithServiceClassPaginationAndDTOFormService.createEntityWithServiceClassPaginationAndDTOFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithServiceClassPaginationAndDTO }) => {
      this.entityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTO;
      if (entityWithServiceClassPaginationAndDTO) {
        this.updateForm(entityWithServiceClassPaginationAndDTO);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithServiceClassPaginationAndDTO =
      this.entityWithServiceClassPaginationAndDTOFormService.getEntityWithServiceClassPaginationAndDTO(this.editForm);
    if (entityWithServiceClassPaginationAndDTO.id === null) {
      this.subscribeToSaveResponse(this.entityWithServiceClassPaginationAndDTOService.create(entityWithServiceClassPaginationAndDTO));
    } else {
      this.subscribeToSaveResponse(this.entityWithServiceClassPaginationAndDTOService.update(entityWithServiceClassPaginationAndDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithServiceClassPaginationAndDTO | null>): void {
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

  protected updateForm(entityWithServiceClassPaginationAndDTO: IEntityWithServiceClassPaginationAndDTO): void {
    this.entityWithServiceClassPaginationAndDTO = entityWithServiceClassPaginationAndDTO;
    this.entityWithServiceClassPaginationAndDTOFormService.resetForm(this.editForm, entityWithServiceClassPaginationAndDTO);
  }
}
