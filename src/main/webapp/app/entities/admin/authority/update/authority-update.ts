import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { AuthorityFormService, AuthorityFormGroup } from './authority-form.service';
import { IAuthority } from '../authority.model';
import { AuthorityService } from '../service/authority.service';

@Component({
  selector: 'jhi-authority-update',
  templateUrl: './authority-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AuthorityUpdate implements OnInit {
  readonly isSaving = signal(false);
  authority: IAuthority | null = null;

  protected authorityService = inject(AuthorityService);
  protected authorityFormService = inject(AuthorityFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AuthorityFormGroup = this.authorityFormService.createAuthorityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authority }) => {
      this.authority = authority;
      if (authority) {
        this.updateForm(authority);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const authority = this.authorityFormService.getAuthority(this.editForm);
    this.subscribeToSaveResponse(this.authorityService.create(authority));
  }

  protected subscribeToSaveResponse(result: Observable<IAuthority | null>): void {
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

  protected updateForm(authority: IAuthority): void {
    this.authority = authority;
    this.authorityFormService.resetForm(this.editForm, authority);
  }
}
