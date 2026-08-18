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

import { FieldTestEntityFormService, FieldTestEntityFormGroup } from './field-test-entity-form.service';
import { IFieldTestEntity } from '../field-test-entity.model';
import { FieldTestEntityService } from '../service/field-test-entity.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnumFieldClass } from 'app/entities/enumerations/enum-field-class.model';
import { EnumRequiredFieldClass } from 'app/entities/enumerations/enum-required-field-class.model';

@Component({
  selector: 'jhi-field-test-entity-update',
  templateUrl: './field-test-entity-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class FieldTestEntityUpdate implements OnInit {
  readonly isSaving = signal(false);
  fieldTestEntity: IFieldTestEntity | null = null;
  enumFieldClassValues = Object.keys(EnumFieldClass);
  enumRequiredFieldClassValues = Object.keys(EnumRequiredFieldClass);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fieldTestEntityService = inject(FieldTestEntityService);
  protected fieldTestEntityFormService = inject(FieldTestEntityFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FieldTestEntityFormGroup = this.fieldTestEntityFormService.createFieldTestEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fieldTestEntity }) => {
      this.fieldTestEntity = fieldTestEntity;
      if (fieldTestEntity) {
        this.updateForm(fieldTestEntity);
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
    const fieldTestEntity = this.fieldTestEntityFormService.getFieldTestEntity(this.editForm);
    if (fieldTestEntity.id === null) {
      this.subscribeToSaveResponse(this.fieldTestEntityService.create(fieldTestEntity));
    } else {
      this.subscribeToSaveResponse(this.fieldTestEntityService.update(fieldTestEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFieldTestEntity | null>): void {
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

  protected updateForm(fieldTestEntity: IFieldTestEntity): void {
    this.fieldTestEntity = fieldTestEntity;
    this.fieldTestEntityFormService.resetForm(this.editForm, fieldTestEntity);
  }
}
