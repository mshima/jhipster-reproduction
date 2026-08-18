import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { IMapsIdUserProfileWithDTO } from '../maps-id-user-profile-with-dto.model';
import { MapsIdUserProfileWithDTOService } from '../service/maps-id-user-profile-with-dto.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './maps-id-user-profile-with-dto-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class MapsIdUserProfileWithDTODeleteDialog {
  mapsIdUserProfileWithDTO?: IMapsIdUserProfileWithDTO;

  protected readonly mapsIdUserProfileWithDTOService = inject(MapsIdUserProfileWithDTOService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mapsIdUserProfileWithDTOService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
