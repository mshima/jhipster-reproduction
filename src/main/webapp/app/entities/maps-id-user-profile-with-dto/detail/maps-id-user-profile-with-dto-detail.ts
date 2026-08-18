import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';

@Component({
  selector: 'jhi-maps-id-user-profile-with-dto-detail',
  templateUrl: './maps-id-user-profile-with-dto-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class MapsIdUserProfileWithDTODetail {
  readonly mapsIdUserProfileWithDTO = input<IMapsIdUserProfileWithDTO | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
