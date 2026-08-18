import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import {
  EntityWithServiceImplAndPaginationFormService,
  EntityWithServiceImplAndPaginationFormGroup,
} from './entity-with-service-impl-and-pagination-form.service';
import { IEntityWithServiceImplAndPagination } from '../entity-with-service-impl-and-pagination.model';
import { EntityWithServiceImplAndPaginationService } from '../service/entity-with-service-impl-and-pagination.service';

@Component({
  selector: 'jhi-entity-with-service-impl-and-pagination-update',
  templateUrl: './entity-with-service-impl-and-pagination-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithServiceImplAndPaginationUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination | null = null;

  protected entityWithServiceImplAndPaginationService = inject(EntityWithServiceImplAndPaginationService);
  protected entityWithServiceImplAndPaginationFormService = inject(EntityWithServiceImplAndPaginationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithServiceImplAndPaginationFormGroup =
    this.entityWithServiceImplAndPaginationFormService.createEntityWithServiceImplAndPaginationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithServiceImplAndPagination }) => {
      this.entityWithServiceImplAndPagination = entityWithServiceImplAndPagination;
      if (entityWithServiceImplAndPagination) {
        this.updateForm(entityWithServiceImplAndPagination);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithServiceImplAndPagination = this.entityWithServiceImplAndPaginationFormService.getEntityWithServiceImplAndPagination(
      this.editForm,
    );
    if (entityWithServiceImplAndPagination.id === null) {
      this.subscribeToSaveResponse(this.entityWithServiceImplAndPaginationService.create(entityWithServiceImplAndPagination));
    } else {
      this.subscribeToSaveResponse(this.entityWithServiceImplAndPaginationService.update(entityWithServiceImplAndPagination));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithServiceImplAndPagination | null>): void {
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

  protected updateForm(entityWithServiceImplAndPagination: IEntityWithServiceImplAndPagination): void {
    this.entityWithServiceImplAndPagination = entityWithServiceImplAndPagination;
    this.entityWithServiceImplAndPaginationFormService.resetForm(this.editForm, entityWithServiceImplAndPagination);
  }
}
