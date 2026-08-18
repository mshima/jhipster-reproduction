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

import {
  FieldTestMapstructAndServiceClassEntityFormService,
  FieldTestMapstructAndServiceClassEntityFormGroup,
} from './field-test-mapstruct-and-service-class-entity-form.service';
import { IFieldTestMapstructAndServiceClassEntity } from '../field-test-mapstruct-and-service-class-entity.model';
import { FieldTestMapstructAndServiceClassEntityService } from '../service/field-test-mapstruct-and-service-class-entity.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnumFieldClass } from 'app/entities/enumerations/enum-field-class.model';
import { EnumRequiredFieldClass } from 'app/entities/enumerations/enum-required-field-class.model';

@Component({
  selector: 'jhi-field-test-mapstruct-and-service-class-entity-update',
  templateUrl: './field-test-mapstruct-and-service-class-entity-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class FieldTestMapstructAndServiceClassEntityUpdate implements OnInit {
  readonly isSaving = signal(false);
  fieldTestMapstructAndServiceClassEntity: IFieldTestMapstructAndServiceClassEntity | null = null;
  enumFieldClassValues = Object.keys(EnumFieldClass);
  enumRequiredFieldClassValues = Object.keys(EnumRequiredFieldClass);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fieldTestMapstructAndServiceClassEntityService = inject(FieldTestMapstructAndServiceClassEntityService);
  protected fieldTestMapstructAndServiceClassEntityFormService = inject(FieldTestMapstructAndServiceClassEntityFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FieldTestMapstructAndServiceClassEntityFormGroup =
    this.fieldTestMapstructAndServiceClassEntityFormService.createFieldTestMapstructAndServiceClassEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fieldTestMapstructAndServiceClassEntity }) => {
      this.fieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntity;
      if (fieldTestMapstructAndServiceClassEntity) {
        this.updateForm(fieldTestMapstructAndServiceClassEntity);
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
    const fieldTestMapstructAndServiceClassEntity =
      this.fieldTestMapstructAndServiceClassEntityFormService.getFieldTestMapstructAndServiceClassEntity(this.editForm);
    if (fieldTestMapstructAndServiceClassEntity.id === null) {
      this.subscribeToSaveResponse(this.fieldTestMapstructAndServiceClassEntityService.create(fieldTestMapstructAndServiceClassEntity));
    } else {
      this.subscribeToSaveResponse(this.fieldTestMapstructAndServiceClassEntityService.update(fieldTestMapstructAndServiceClassEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFieldTestMapstructAndServiceClassEntity | null>): void {
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

  protected updateForm(fieldTestMapstructAndServiceClassEntity: IFieldTestMapstructAndServiceClassEntity): void {
    this.fieldTestMapstructAndServiceClassEntity = fieldTestMapstructAndServiceClassEntity;
    this.fieldTestMapstructAndServiceClassEntityFormService.resetForm(this.editForm, fieldTestMapstructAndServiceClassEntity);
  }
}
