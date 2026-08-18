import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { TranslatePipe } from '@ngx-translate/core';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { FieldTestEnumWithValueFormService, FieldTestEnumWithValueFormGroup } from './field-test-enum-with-value-form.service';
import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';
import { FieldTestEnumWithValueService } from '../service/field-test-enum-with-value.service';
import { MyEnumA } from 'app/entities/enumerations/my-enum-a.model';
import { MyEnumB } from 'app/entities/enumerations/my-enum-b.model';
import { MyEnumC } from 'app/entities/enumerations/my-enum-c.model';
import { MyEnumD } from 'app/entities/enumerations/my-enum-d.model';
import { MyEnumE } from 'app/entities/enumerations/my-enum-e.model';

@Component({
  selector: 'jhi-field-test-enum-with-value-update',
  templateUrl: './field-test-enum-with-value-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FieldTestEnumWithValueUpdate implements OnInit {
  readonly isSaving = signal(false);
  fieldTestEnumWithValue: IFieldTestEnumWithValue | null = null;
  myEnumAValues = Object.keys(MyEnumA);
  myEnumBValues = Object.keys(MyEnumB);
  myEnumCValues = Object.keys(MyEnumC);
  myEnumDValues = Object.keys(MyEnumD);
  myEnumEValues = Object.keys(MyEnumE);

  protected fieldTestEnumWithValueService = inject(FieldTestEnumWithValueService);
  protected fieldTestEnumWithValueFormService = inject(FieldTestEnumWithValueFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FieldTestEnumWithValueFormGroup = this.fieldTestEnumWithValueFormService.createFieldTestEnumWithValueFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fieldTestEnumWithValue }) => {
      this.fieldTestEnumWithValue = fieldTestEnumWithValue;
      if (fieldTestEnumWithValue) {
        this.updateForm(fieldTestEnumWithValue);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const fieldTestEnumWithValue = this.fieldTestEnumWithValueFormService.getFieldTestEnumWithValue(this.editForm);
    if (fieldTestEnumWithValue.id === null) {
      this.subscribeToSaveResponse(this.fieldTestEnumWithValueService.create(fieldTestEnumWithValue));
    } else {
      this.subscribeToSaveResponse(this.fieldTestEnumWithValueService.update(fieldTestEnumWithValue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFieldTestEnumWithValue | null>): void {
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

  protected updateForm(fieldTestEnumWithValue: IFieldTestEnumWithValue): void {
    this.fieldTestEnumWithValue = fieldTestEnumWithValue;
    this.fieldTestEnumWithValueFormService.resetForm(this.editForm, fieldTestEnumWithValue);
  }
}
