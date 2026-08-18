import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IFieldTestInfiniteScrollEntity } from '../field-test-infinite-scroll-entity.model';
import { FieldTestInfiniteScrollEntityService } from '../service/field-test-infinite-scroll-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './field-test-infinite-scroll-entity-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class FieldTestInfiniteScrollEntityDeleteDialog {
  fieldTestInfiniteScrollEntity?: IFieldTestInfiniteScrollEntity;

  protected readonly fieldTestInfiniteScrollEntityService = inject(FieldTestInfiniteScrollEntityService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fieldTestInfiniteScrollEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
