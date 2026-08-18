import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { EntityWithPaginationAndDTOFormService, EntityWithPaginationAndDTOFormGroup } from './entity-with-pagination-and-dto-form.service';
import { IEntityWithPaginationAndDTO } from '../entity-with-pagination-and-dto.model';
import { EntityWithPaginationAndDTOService } from '../service/entity-with-pagination-and-dto.service';

@Component({
  selector: 'jhi-entity-with-pagination-and-dto-update',
  templateUrl: './entity-with-pagination-and-dto-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithPaginationAndDTOUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithPaginationAndDTO: IEntityWithPaginationAndDTO | null = null;

  protected entityWithPaginationAndDTOService = inject(EntityWithPaginationAndDTOService);
  protected entityWithPaginationAndDTOFormService = inject(EntityWithPaginationAndDTOFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithPaginationAndDTOFormGroup = this.entityWithPaginationAndDTOFormService.createEntityWithPaginationAndDTOFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithPaginationAndDTO }) => {
      this.entityWithPaginationAndDTO = entityWithPaginationAndDTO;
      if (entityWithPaginationAndDTO) {
        this.updateForm(entityWithPaginationAndDTO);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithPaginationAndDTO = this.entityWithPaginationAndDTOFormService.getEntityWithPaginationAndDTO(this.editForm);
    if (entityWithPaginationAndDTO.id === null) {
      this.subscribeToSaveResponse(this.entityWithPaginationAndDTOService.create(entityWithPaginationAndDTO));
    } else {
      this.subscribeToSaveResponse(this.entityWithPaginationAndDTOService.update(entityWithPaginationAndDTO));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithPaginationAndDTO | null>): void {
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

  protected updateForm(entityWithPaginationAndDTO: IEntityWithPaginationAndDTO): void {
    this.entityWithPaginationAndDTO = entityWithPaginationAndDTO;
    this.entityWithPaginationAndDTOFormService.resetForm(this.editForm, entityWithPaginationAndDTO);
  }
}
