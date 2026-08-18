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
import { FormsModule } from '@angular/forms';
import { IFieldTestInfiniteScrollEntity } from '../field-test-infinite-scroll-entity.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { FieldTestInfiniteScrollEntityService } from '../service/field-test-infinite-scroll-entity.service';
import { FieldTestInfiniteScrollEntityDeleteDialog } from '../delete/field-test-infinite-scroll-entity-delete-dialog';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';

@Component({
  selector: 'jhi-field-test-infinite-scroll-entity',
  templateUrl: './field-test-infinite-scroll-entity.html',
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
    InfiniteScrollDirective,
  ],
})
export class FieldTestInfiniteScrollEntity implements OnInit {
  subscription: Subscription | null = null;
  readonly fieldTestInfiniteScrollEntities = signal<IFieldTestInfiniteScrollEntity[]>([]);

  sortState = sortStateSignal({});

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly links: WritableSignal<Record<string, undefined | Record<string, string | undefined>>> = signal({});
  readonly hasMorePage = computed(() => !!this.links().next);
  readonly isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  readonly router = inject(Router);
  protected readonly fieldTestInfiniteScrollEntityService = inject(FieldTestInfiniteScrollEntityService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.fieldTestInfiniteScrollEntityService.fieldTestInfiniteScrollEntitiesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.fieldTestInfiniteScrollEntityService.fieldTestInfiniteScrollEntitiesResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.fieldTestInfiniteScrollEntities.update(fieldTestInfiniteScrollEntities =>
        this.fillComponentAttributesFromResponseBody(
          [...this.fieldTestInfiniteScrollEntityService.fieldTestInfiniteScrollEntities()],
          fieldTestInfiniteScrollEntities,
        ),
      );
    });
  }

  trackId = (item: IFieldTestInfiniteScrollEntity): number =>
    this.fieldTestInfiniteScrollEntityService.getFieldTestInfiniteScrollEntityIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  reset(): void {
    this.fieldTestInfiniteScrollEntities.set([]);
  }

  loadNextPage(): void {
    this.load();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(fieldTestInfiniteScrollEntity: IFieldTestInfiniteScrollEntity): void {
    const modalRef = this.modalService.open(FieldTestInfiniteScrollEntityDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fieldTestInfiniteScrollEntity = fieldTestInfiniteScrollEntity;
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

  protected fillComponentAttributesFromResponseBody(
    data: IFieldTestInfiniteScrollEntity[],
    currentValue: IFieldTestInfiniteScrollEntity[],
  ): IFieldTestInfiniteScrollEntity[] {
    const fieldTestInfiniteScrollEntitiesNew = [...currentValue];
    for (const d of data) {
      if (!fieldTestInfiniteScrollEntitiesNew.some(op => op.id === d.id)) {
        fieldTestInfiniteScrollEntitiesNew.push(d);
      }
    }
    return fieldTestInfiniteScrollEntitiesNew;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): void {
    const queryObject: any = {
      size: this.itemsPerPage(),
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    this.fieldTestInfiniteScrollEntityService.fieldTestInfiniteScrollEntitiesParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
