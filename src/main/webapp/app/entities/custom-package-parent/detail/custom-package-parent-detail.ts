import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICustomPackageParent } from '../custom-package-parent.model';

@Component({
  selector: 'jhi-custom-package-parent-detail',
  templateUrl: './custom-package-parent-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CustomPackageParentDetail {
  readonly customPackageParent = input<ICustomPackageParent | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
