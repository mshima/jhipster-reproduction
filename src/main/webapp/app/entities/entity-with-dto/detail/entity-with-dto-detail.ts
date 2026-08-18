import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEntityWithDTO } from '../entity-with-dto.model';

@Component({
  selector: 'jhi-entity-with-dto-detail',
  templateUrl: './entity-with-dto-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class EntityWithDTODetail {
  readonly entityWithDTO = input<IEntityWithDTO | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
