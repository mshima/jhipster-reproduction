import { Component, computed, effect, inject, OnInit, signal, WritableSignal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';
import { combineLatest, filter, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { AlertError } from 'app/shared/alert/alert-error';
import { Alert } from 'app/shared/alert/alert';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { IFieldTestEnumWithValue } from '../field-test-enum-with-value.model';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { FieldTestEnumWithValueService } from '../service/field-test-enum-with-value.service';
import { FieldTestEnumWithValueDeleteDialog } from '../delete/field-test-enum-with-value-delete-dialog';

@Component({
  selector: 'jhi-field-test-enum-with-value',
  templateUrl: './field-test-enum-with-value.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class FieldTestEnumWithValue implements OnInit {
  subscription: Subscription | null = null;
  readonly fieldTestEnumWithValues = signal<IFieldTestEnumWithValue[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly fieldTestEnumWithValueService = inject(FieldTestEnumWithValueService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.fieldTestEnumWithValueService.fieldTestEnumWithValuesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.fieldTestEnumWithValues.set(
        this.fillComponentAttributesFromResponseBody([...this.fieldTestEnumWithValueService.fieldTestEnumWithValues()]),
      );
    });
  }

  trackId = (item: IFieldTestEnumWithValue): number => this.fieldTestEnumWithValueService.getFieldTestEnumWithValueIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.fieldTestEnumWithValues().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(fieldTestEnumWithValue: IFieldTestEnumWithValue): void {
    const modalRef = this.modalService.open(FieldTestEnumWithValueDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fieldTestEnumWithValue = fieldTestEnumWithValue;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IFieldTestEnumWithValue[]): IFieldTestEnumWithValue[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IFieldTestEnumWithValue[]): IFieldTestEnumWithValue[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.fieldTestEnumWithValueService.fieldTestEnumWithValuesParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
