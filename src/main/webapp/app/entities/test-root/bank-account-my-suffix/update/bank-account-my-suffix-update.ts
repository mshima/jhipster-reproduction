import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, map, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { TranslatePipe } from '@ngx-translate/core';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { BankAccountMySuffixFormService, BankAccountMySuffixFormGroup } from './bank-account-my-suffix-form.service';
import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';

@Component({
  selector: 'jhi-bank-account-my-suffix-update',
  templateUrl: './bank-account-my-suffix-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class BankAccountMySuffixUpdate implements OnInit {
  readonly isSaving = signal(false);
  bankAccount: IBankAccountMySuffix | null = null;
  bankAccountTypeValues = Object.keys(BankAccountType);

  usersSharedCollection = signal<IUser[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected bankAccountService = inject(BankAccountMySuffixService);
  protected bankAccountFormService = inject(BankAccountMySuffixFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankAccountMySuffixFormGroup = this.bankAccountFormService.createBankAccountMySuffixFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.bankAccount = bankAccount;
      if (bankAccount) {
        this.updateForm(bankAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertErrorModel>('sampleWebfluxH2MemApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const bankAccount = this.bankAccountFormService.getBankAccountMySuffix(this.editForm);
    if (bankAccount.id === null) {
      this.subscribeToSaveResponse(this.bankAccountService.create(bankAccount));
    } else {
      this.subscribeToSaveResponse(this.bankAccountService.update(bankAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IBankAccountMySuffix | null>): void {
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

  protected updateForm(bankAccount: IBankAccountMySuffix): void {
    this.bankAccount = bankAccount;
    this.bankAccountFormService.resetForm(this.editForm, bankAccount);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, bankAccount.user));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bankAccount?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
