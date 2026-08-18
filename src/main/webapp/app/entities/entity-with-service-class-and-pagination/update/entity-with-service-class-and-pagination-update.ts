import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import {
  EntityWithServiceClassAndPaginationFormService,
  EntityWithServiceClassAndPaginationFormGroup,
} from './entity-with-service-class-and-pagination-form.service';
import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';
import { EntityWithServiceClassAndPaginationService } from '../service/entity-with-service-class-and-pagination.service';

@Component({
  selector: 'jhi-entity-with-service-class-and-pagination-update',
  templateUrl: './entity-with-service-class-and-pagination-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EntityWithServiceClassAndPaginationUpdate implements OnInit {
  readonly isSaving = signal(false);
  entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination | null = null;

  protected entityWithServiceClassAndPaginationService = inject(EntityWithServiceClassAndPaginationService);
  protected entityWithServiceClassAndPaginationFormService = inject(EntityWithServiceClassAndPaginationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EntityWithServiceClassAndPaginationFormGroup =
    this.entityWithServiceClassAndPaginationFormService.createEntityWithServiceClassAndPaginationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entityWithServiceClassAndPagination }) => {
      this.entityWithServiceClassAndPagination = entityWithServiceClassAndPagination;
      if (entityWithServiceClassAndPagination) {
        this.updateForm(entityWithServiceClassAndPagination);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const entityWithServiceClassAndPagination = this.entityWithServiceClassAndPaginationFormService.getEntityWithServiceClassAndPagination(
      this.editForm,
    );
    if (entityWithServiceClassAndPagination.id === null) {
      this.subscribeToSaveResponse(this.entityWithServiceClassAndPaginationService.create(entityWithServiceClassAndPagination));
    } else {
      this.subscribeToSaveResponse(this.entityWithServiceClassAndPaginationService.update(entityWithServiceClassAndPagination));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEntityWithServiceClassAndPagination | null>): void {
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

  protected updateForm(entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination): void {
    this.entityWithServiceClassAndPagination = entityWithServiceClassAndPagination;
    this.entityWithServiceClassAndPaginationFormService.resetForm(this.editForm, entityWithServiceClassAndPagination);
  }
}
