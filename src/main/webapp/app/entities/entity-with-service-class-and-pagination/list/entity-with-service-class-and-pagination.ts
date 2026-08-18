import { Component, computed, effect, inject, OnInit, signal, WritableSignal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';
import { combineLatest, filter, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { AlertError } from 'app/shared/alert/alert-error';
import { Alert } from 'app/shared/alert/alert';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { ItemCount } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { IEntityWithServiceClassAndPagination } from '../entity-with-service-class-and-pagination.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityWithServiceClassAndPaginationService } from '../service/entity-with-service-class-and-pagination.service';
import { EntityWithServiceClassAndPaginationDeleteDialog } from '../delete/entity-with-service-class-and-pagination-delete-dialog';

@Component({
  selector: 'jhi-entity-with-service-class-and-pagination',
  templateUrl: './entity-with-service-class-and-pagination.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    NgbPagination,
    ItemCount,
  ],
})
export class EntityWithServiceClassAndPagination implements OnInit {
  subscription: Subscription | null = null;
  readonly entityWithServiceClassAndPaginations = signal<IEntityWithServiceClassAndPagination[]>([]);

  sortState = sortStateSignal({});

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly entityWithServiceClassAndPaginationService = inject(EntityWithServiceClassAndPaginationService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.entityWithServiceClassAndPaginationService.entityWithServiceClassAndPaginationsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.entityWithServiceClassAndPaginationService.entityWithServiceClassAndPaginationsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.entityWithServiceClassAndPaginations.set(
        this.fillComponentAttributesFromResponseBody([
          ...this.entityWithServiceClassAndPaginationService.entityWithServiceClassAndPaginations(),
        ]),
      );
    });
  }

  trackId = (item: IEntityWithServiceClassAndPagination): number =>
    this.entityWithServiceClassAndPaginationService.getEntityWithServiceClassAndPaginationIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(entityWithServiceClassAndPagination: IEntityWithServiceClassAndPagination): void {
    const modalRef = this.modalService.open(EntityWithServiceClassAndPaginationDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entityWithServiceClassAndPagination = entityWithServiceClassAndPagination;
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
    this.handleNavigation(this.page(), event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected fillComponentAttributesFromResponseBody(data: IEntityWithServiceClassAndPagination[]): IEntityWithServiceClassAndPagination[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.entityWithServiceClassAndPaginationService.entityWithServiceClassAndPaginationsParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
