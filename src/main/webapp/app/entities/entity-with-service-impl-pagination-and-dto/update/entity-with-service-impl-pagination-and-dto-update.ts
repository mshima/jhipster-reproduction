import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import {
  EntityWithServiceImplPaginationAndDTOFormService,
  EntityWithServiceImplPaginationAndDTOFormGroup,
} from './entity-with-service-impl-pagination-and-dto-form.service';
import { IEntityWithServiceImplPaginationAndDTO } from '../entity-with-service-impl-pagination-and-dto.model';
import { EntityWithServiceImplPaginationAndDTOService } from '../service/entity-with-service-impl-pagination-and-dto.service';

@Component({
  selector: 'jhi-entity-with-service-impl-pagination-and-dto-update',
  templateUrl: './entity-with-service-impl-pagination-and-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithServiceImplPaginationAndDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO | null = null;

  protected entityWithServiceImplPaginationAndDTOService = inject(EntityWithServiceImplPaginationAndDTOService);
  protected entityWithServiceImplPaginationAndDTOFormService = inject(EntityWithServiceImplPaginationAndDTOFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithServiceImplPaginationAndDTOFormGroup =
    this.entityWithServiceImplPaginationAndDTOFormService.createEntityWithServiceImplPaginationAndDTOFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithServiceImplPaginationAndDTO }) => {
      this.entityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTO;
      if (entityWithServiceImplPaginationAndDTO) {
        this.updateForm(entityWithServiceImplPaginationAndDTO);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithServiceImplPaginationAndDTO =
      this.entityWithServiceImplPaginationAndDTOFormService.getEntityWithServiceImplPaginationAndDTO(this.editForm);
    if (entityWithServiceImplPaginationAndDTO.id === null) {
      this.subscribeToSaveResponse(this.entityWithServiceImplPaginationAndDTOService.create(entityWithServiceImplPaginationAndDTO));
    } else {
      this.subscribeToSaveResponse(this.entityWithServiceImplPaginationAndDTOService.update(entityWithServiceImplPaginationAndDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithServiceImplPaginationAndDTO | null>): void {
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

  protected updateForm(entityWithServiceImplPaginationAndDTO: IEntityWithServiceImplPaginationAndDTO): void {
    this.entityWithServiceImplPaginationAndDTO = entityWithServiceImplPaginationAndDTO;
    this.entityWithServiceImplPaginationAndDTOFormService.resetForm(this.editForm, entityWithServiceImplPaginationAndDTO);
  }
}
