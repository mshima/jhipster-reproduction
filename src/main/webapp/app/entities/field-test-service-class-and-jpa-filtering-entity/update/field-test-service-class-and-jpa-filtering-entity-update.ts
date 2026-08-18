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
  FieldTestServiceClassAndJpaFilteringEntityFormService,
  FieldTestServiceClassAndJpaFilteringEntityFormGroup,
} from './field-test-service-class-and-jpa-filtering-entity-form.service';
import { IFieldTestServiceClassAndJpaFilteringEntity } from '../field-test-service-class-and-jpa-filtering-entity.model';
import { FieldTestServiceClassAndJpaFilteringEntityService } from '../service/field-test-service-class-and-jpa-filtering-entity.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnumFieldClass } from 'app/entities/enumerations/enum-field-class.model';
import { EnumRequiredFieldClass } from 'app/entities/enumerations/enum-required-field-class.model';

@Component({
  selector: 'jhi-field-test-service-class-and-jpa-filtering-entity-update',
  templateUrl: './field-test-service-class-and-jpa-filtering-entity-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class FieldTestServiceClassAndJpaFilteringEntityUpdate implements OnInit {
  readonly isSaving = signal(false);
  fieldTestServiceClassAndJpaFilteringEntity: IFieldTestServiceClassAndJpaFilteringEntity | null = null;
  enumFieldClassValues = Object.keys(EnumFieldClass);
  enumRequiredFieldClassValues = Object.keys(EnumRequiredFieldClass);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fieldTestServiceClassAndJpaFilteringEntityService = inject(FieldTestServiceClassAndJpaFilteringEntityService);
  protected fieldTestServiceClassAndJpaFilteringEntityFormService = inject(FieldTestServiceClassAndJpaFilteringEntityFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FieldTestServiceClassAndJpaFilteringEntityFormGroup =
    this.fieldTestServiceClassAndJpaFilteringEntityFormService.createFieldTestServiceClassAndJpaFilteringEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fieldTestServiceClassAndJpaFilteringEntity }) => {
      this.fieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntity;
      if (fieldTestServiceClassAndJpaFilteringEntity) {
        this.updateForm(fieldTestServiceClassAndJpaFilteringEntity);
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
    const fieldTestServiceClassAndJpaFilteringEntity =
      this.fieldTestServiceClassAndJpaFilteringEntityFormService.getFieldTestServiceClassAndJpaFilteringEntity(this.editForm);
    if (fieldTestServiceClassAndJpaFilteringEntity.id === null) {
      this.subscribeToSaveResponse(
        this.fieldTestServiceClassAndJpaFilteringEntityService.create(fieldTestServiceClassAndJpaFilteringEntity),
      );
    } else {
      this.subscribeToSaveResponse(
        this.fieldTestServiceClassAndJpaFilteringEntityService.update(fieldTestServiceClassAndJpaFilteringEntity),
      );
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFieldTestServiceClassAndJpaFilteringEntity | null>): void {
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

  protected updateForm(fieldTestServiceClassAndJpaFilteringEntity: IFieldTestServiceClassAndJpaFilteringEntity): void {
    this.fieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntity;
    this.fieldTestServiceClassAndJpaFilteringEntityFormService.resetForm(this.editForm, fieldTestServiceClassAndJpaFilteringEntity);
  }
}
