import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEntityWithServiceImplAndPagination } from '../entity-with-service-impl-and-pagination.model';

@Component({
  selector: 'jhi-entity-with-service-impl-and-pagination-detail',
  templateUrl: './entity-with-service-impl-and-pagination-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class EntityWithServiceImplAndPaginationDetail {
  readonly entityWithServiceImplAndPagination = input<IEntityWithServiceImplAndPagination | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
