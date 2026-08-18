import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { finalize, map, Observable } from 'rxjs';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { AlertError } from 'app/shared/alert/alert-error';
import { ReactiveFormsModule } from '@angular/forms';

import { OperationFormService, OperationFormGroup } from './operation-form.service';
import { IOperation } from '../operation.model';
import { OperationService } from '../service/operation.service';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { BankAccountMySuffixService } from 'app/entities/test-root/bank-account-my-suffix/service/bank-account-my-suffix.service';
import { ILabel } from 'app/entities/test-root/label/label.model';
import { LabelService } from 'app/entities/test-root/label/service/label.service';

@Component({
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OperationUpdate implements OnInit {
  readonly isSaving = signal(false);
  operation: IOperation | null = null;

  bankAccountsSharedCollection = signal<IBankAccountMySuffix[]>([]);
  labelsSharedCollection = signal<ILabel[]>([]);

  protected operationService = inject(OperationService);
  protected operationFormService = inject(OperationFormService);
  protected bankAccountService = inject(BankAccountMySuffixService);
  protected labelService = inject(LabelService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OperationFormGroup = this.operationFormService.createOperationFormGroup();

  compareBankAccountMySuffix = (o1: IBankAccountMySuffix | null, o2: IBankAccountMySuffix | null): boolean =>
    this.bankAccountService.compareBankAccountMySuffix(o1, o2);

  compareLabel = (o1: ILabel | null, o2: ILabel | null): boolean => this.labelService.compareLabel(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      this.operation = operation;
      if (operation) {
        this.updateForm(operation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const operation = this.operationFormService.getOperation(this.editForm);
    if (operation.id === null) {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOperation | null>): void {
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

  protected updateForm(operation: IOperation): void {
    this.operation = operation;
    this.operationFormService.resetForm(this.editForm, operation);

    this.bankAccountsSharedCollection.update(bankAccounts =>
      this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing<IBankAccountMySuffix>(bankAccounts, operation.bankAccount),
    );
    this.labelsSharedCollection.update(labels =>
      this.labelService.addLabelToCollectionIfMissing<ILabel>(labels, ...(operation.labels ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankAccountService
      .query()
      .pipe(map((res: HttpResponse<IBankAccountMySuffix[]>) => res.body ?? []))
      .pipe(
        map((bankAccounts: IBankAccountMySuffix[]) =>
          this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing<IBankAccountMySuffix>(
            bankAccounts,
            this.operation?.bankAccount,
          ),
        ),
      )
      .subscribe((bankAccounts: IBankAccountMySuffix[]) => this.bankAccountsSharedCollection.set(bankAccounts));

    this.labelService
      .query()
      .pipe(map((res: HttpResponse<ILabel[]>) => res.body ?? []))
      .pipe(map((labels: ILabel[]) => this.labelService.addLabelToCollectionIfMissing<ILabel>(labels, ...(this.operation?.labels ?? []))))
      .subscribe((labels: ILabel[]) => this.labelsSharedCollection.set(labels));
  }
}
