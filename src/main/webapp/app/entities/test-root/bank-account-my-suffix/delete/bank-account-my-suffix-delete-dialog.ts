import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bank-account-my-suffix-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class BankAccountMySuffixDeleteDialog {
  bankAccount?: IBankAccountMySuffix;

  protected readonly bankAccountService = inject(BankAccountMySuffixService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
