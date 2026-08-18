import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IUserManagement } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-management-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class UserManagementDeleteDialog {
  userManagement?: IUserManagement;

  protected readonly userManagementService = inject(UserManagementService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(login: string): void {
    this.userManagementService.delete(login).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
