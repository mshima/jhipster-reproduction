import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';

@Component({
  selector: 'jhi-entity-with-service-class-and-pagination-detail',
  templateUrl: './entity-with-service-class-and-pagination-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class EntityWithServiceClassAndPaginationDetail {
  readonly entityWithServiceClassAndPagination = input<IEntityWithServiceClassAndPagination | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
