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
import { IFieldTestServiceClassAndJpaFilteringEntity } from '../field-test-service-class-and-jpa-filtering-entity.model';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { FieldTestServiceClassAndJpaFilteringEntityService } from '../service/field-test-service-class-and-jpa-filtering-entity.service';
import { FieldTestServiceClassAndJpaFilteringEntityDeleteDialog } from '../delete/field-test-service-class-and-jpa-filtering-entity-delete-dialog';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-field-test-service-class-and-jpa-filtering-entity',
  templateUrl: './field-test-service-class-and-jpa-filtering-entity.html',
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
export class FieldTestServiceClassAndJpaFilteringEntity implements OnInit {
  subscription: Subscription | null = null;
  readonly fieldTestServiceClassAndJpaFilteringEntities = signal<IFieldTestServiceClassAndJpaFilteringEntity[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly fieldTestServiceClassAndJpaFilteringEntityService = inject(FieldTestServiceClassAndJpaFilteringEntityService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading =
    this.fieldTestServiceClassAndJpaFilteringEntityService.fieldTestServiceClassAndJpaFilteringEntitiesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.fieldTestServiceClassAndJpaFilteringEntities.set(
        this.fillComponentAttributesFromResponseBody([
          ...this.fieldTestServiceClassAndJpaFilteringEntityService.fieldTestServiceClassAndJpaFilteringEntities(),
        ]),
      );
    });
  }

  trackId = (item: IFieldTestServiceClassAndJpaFilteringEntity): number =>
    this.fieldTestServiceClassAndJpaFilteringEntityService.getFieldTestServiceClassAndJpaFilteringEntityIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(fieldTestServiceClassAndJpaFilteringEntity: IFieldTestServiceClassAndJpaFilteringEntity): void {
    const modalRef = this.modalService.open(FieldTestServiceClassAndJpaFilteringEntityDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fieldTestServiceClassAndJpaFilteringEntity = fieldTestServiceClassAndJpaFilteringEntity;
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

  protected refineData(data: IFieldTestServiceClassAndJpaFilteringEntity[]): IFieldTestServiceClassAndJpaFilteringEntity[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(
    data: IFieldTestServiceClassAndJpaFilteringEntity[],
  ): IFieldTestServiceClassAndJpaFilteringEntity[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.fieldTestServiceClassAndJpaFilteringEntityService.fieldTestServiceClassAndJpaFilteringEntitiesParams.set(queryObject);
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
