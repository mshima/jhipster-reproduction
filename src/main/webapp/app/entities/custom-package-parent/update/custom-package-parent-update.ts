import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICustomPackageParent } from '../custom-package-parent.model';
import { CustomPackageParentService } from '../service/custom-package-parent.service';

import { CustomPackageParentFormGroup, CustomPackageParentFormService } from './custom-package-parent-form.service';

@Component({
  selector: 'jhi-custom-package-parent-update',
  templateUrl: './custom-package-parent-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustomPackageParentUpdate implements OnInit {
  readonly isSaving = signal(false);
  customPackageParent: ICustomPackageParent | null = null;

  protected customPackageParentService = inject(CustomPackageParentService);
  protected customPackageParentFormService = inject(CustomPackageParentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomPackageParentFormGroup = this.customPackageParentFormService.createCustomPackageParentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customPackageParent }) => {
      this.customPackageParent = customPackageParent;
      if (customPackageParent) {
        this.updateForm(customPackageParent);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const customPackageParent = this.customPackageParentFormService.getCustomPackageParent(this.editForm);
    if (customPackageParent.id === null) {
      this.subscribeToSaveResponse(this.customPackageParentService.create(customPackageParent));
    } else {
      this.subscribeToSaveResponse(this.customPackageParentService.update(customPackageParent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustomPackageParent | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(customPackageParent: ICustomPackageParent): void {
    this.customPackageParent = customPackageParent;
    this.customPackageParentFormService.resetForm(this.editForm, customPackageParent);
  }
}
