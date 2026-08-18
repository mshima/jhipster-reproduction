import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IAuthority } from '../authority.model';
import { AuthorityService } from '../service/authority.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './authority-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class AuthorityDeleteDialog {
  authority?: IAuthority;

  protected readonly authorityService = inject(AuthorityService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(name: string): void {
    this.authorityService.delete(name).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
