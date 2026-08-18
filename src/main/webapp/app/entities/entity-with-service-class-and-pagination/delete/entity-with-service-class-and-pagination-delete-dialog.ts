import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';
import { EntityWithServiceClassAndPaginationService } from '../service/entity-with-service-class-and-pagination.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './entity-with-service-class-and-pagination-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class EntityWithServiceClassAndPaginationDeleteDialog {
  entityWithServiceClassAndPagination?: IEntityWithServiceClassAndPagination;

  protected readonly entityWithServiceClassAndPaginationService = inject(EntityWithServiceClassAndPaginationService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entityWithServiceClassAndPaginationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
