import { Component, inject, signal, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { TranslatePipe } from '@ngx-translate/core';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { FieldTestPaginationEntityFormService, FieldTestPaginationEntityFormGroup } from './field-test-pagination-entity-form.service';
import { IFieldTestPaginationEntity } from '../field-test-pagination-entity.model';
import { FieldTestPaginationEntityService } from '../service/field-test-pagination-entity.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnumFieldClass } from 'app/entities/enumerations/enum-field-class.model';
import { EnumRequiredFieldClass } from 'app/entities/enumerations/enum-required-field-class.model';

@Component({
  selector: 'jhi-field-test-pagination-entity-update',
  templateUrl: './field-test-pagination-entity-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class FieldTestPaginationEntityUpdate implements OnInit {
  readonly isSaving = signal(false);
  fieldTestPaginationEntity: IFieldTestPaginationEntity | null = null;
  enumFieldClassValues = Object.keys(EnumFieldClass);
  enumRequiredFieldClassValues = Object.keys(EnumRequiredFieldClass);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fieldTestPaginationEntityService = inject(FieldTestPaginationEntityService);
  protected fieldTestPaginationEntityFormService = inject(FieldTestPaginationEntityFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FieldTestPaginationEntityFormGroup = this.fieldTestPaginationEntityFormService.createFieldTestPaginationEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fieldTestPaginationEntity }) => {
      this.fieldTestPaginationEntity = fieldTestPaginationEntity;
      if (fieldTestPaginationEntity) {
        this.updateForm(fieldTestPaginationEntity);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertErrorModel>('sampleWebfluxPsqlApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const fieldTestPaginationEntity = this.fieldTestPaginationEntityFormService.getFieldTestPaginationEntity(this.editForm);
    if (fieldTestPaginationEntity.id === null) {
      this.subscribeToSaveResponse(this.fieldTestPaginationEntityService.create(fieldTestPaginationEntity));
    } else {
      this.subscribeToSaveResponse(this.fieldTestPaginationEntityService.update(fieldTestPaginationEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFieldTestPaginationEntity | null>): void {
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

  protected updateForm(fieldTestPaginationEntity: IFieldTestPaginationEntity): void {
    this.fieldTestPaginationEntity = fieldTestPaginationEntity;
    this.fieldTestPaginationEntityFormService.resetForm(this.editForm, fieldTestPaginationEntity);
  }
}
