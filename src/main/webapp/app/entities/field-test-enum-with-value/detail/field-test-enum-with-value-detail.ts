import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';

@Component({
  selector: 'jhi-field-test-enum-with-value-detail',
  templateUrl: './field-test-enum-with-value-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FieldTestEnumWithValueDetail {
  readonly fieldTestEnumWithValue = input<IFieldTestEnumWithValue | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
