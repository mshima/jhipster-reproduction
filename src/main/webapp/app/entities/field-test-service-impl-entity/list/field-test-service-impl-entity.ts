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
import { IFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { FieldTestServiceImplEntityService } from '../service/field-test-service-impl-entity.service';
import { FieldTestServiceImplEntityDeleteDialog } from '../delete/field-test-service-impl-entity-delete-dialog';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-field-test-service-impl-entity',
  templateUrl: './field-test-service-impl-entity.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class FieldTestServiceImplEntity implements OnInit {
  subscription: Subscription | null = null;
  readonly fieldTestServiceImplEntities = signal<IFieldTestServiceImplEntity[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly fieldTestServiceImplEntityService = inject(FieldTestServiceImplEntityService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.fieldTestServiceImplEntityService.fieldTestServiceImplEntitiesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.fieldTestServiceImplEntities.set(
        this.fillComponentAttributesFromResponseBody([...this.fieldTestServiceImplEntityService.fieldTestServiceImplEntities()]),
      );
    });
  }

  trackId = (item: IFieldTestServiceImplEntity): number =>
    this.fieldTestServiceImplEntityService.getFieldTestServiceImplEntityIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.fieldTestServiceImplEntities().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(fieldTestServiceImplEntity: IFieldTestServiceImplEntity): void {
    const modalRef = this.modalService.open(FieldTestServiceImplEntityDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fieldTestServiceImplEntity = fieldTestServiceImplEntity;
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

  protected refineData(data: IFieldTestServiceImplEntity[]): IFieldTestServiceImplEntity[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IFieldTestServiceImplEntity[]): IFieldTestServiceImplEntity[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.fieldTestServiceImplEntityService.fieldTestServiceImplEntitiesParams.set(queryObject);
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
