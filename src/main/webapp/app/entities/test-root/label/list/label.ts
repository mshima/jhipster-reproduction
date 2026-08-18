import { Component, computed, effect, inject, OnInit, signal, WritableSignal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';
import { combineLatest, Subscription, tap } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { Alert } from 'app/shared/alert/alert';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateDirective } from 'app/shared/language';
import { TranslatePipe } from '@ngx-translate/core';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { ItemCount } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ILabel } from '../label.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { SORT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { LabelService } from '../service/label.service';

@Component({
  selector: 'jhi-label',
  templateUrl: './label.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    NgbPagination,
    ItemCount,
  ],
})
export class Label implements OnInit {
  private static readonly NOT_SORTABLE_FIELDS_AFTER_SEARCH = ['labelName'];

  subscription: Subscription | null = null;
  readonly labels = signal<ILabel[]>([]);

  sortState = sortStateSignal({});
  readonly currentSearch = signal('');

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly labelService = inject(LabelService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.labelService.labelsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);

  constructor() {
    effect(() => {
      const headers = this.labelService.labelsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.labels.set(this.fillComponentAttributesFromResponseBody([...this.labelService.labels()]));
    });
  }

  trackId = (item: ILabel): number => this.labelService.getLabelIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  search(query: string): void {
    this.page.set(1);
    this.currentSearch.set(query);
    const { predicate } = this.sortState();
    if (query && predicate && Label.NOT_SORTABLE_FIELDS_AFTER_SEARCH.includes(predicate)) {
      this.navigateToWithComponentValues(this.getDefaultSortState());
      return;
    }
    this.navigateToWithComponentValues(this.sortState());
  }

  getDefaultSortState(): SortState {
    return this.sortService.parseSortParam(this.activatedRoute.snapshot.data[DEFAULT_SORT_DATA]);
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page(), event, this.currentSearch());
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.currentSearch());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    if (params.has('search') && params.get('search') !== '') {
      this.currentSearch.set(params.get('search') as string);
      const { predicate } = this.sortState();
      if (predicate && Label.NOT_SORTABLE_FIELDS_AFTER_SEARCH.includes(predicate)) {
        this.sortState.set({});
      }
    }
  }

  protected fillComponentAttributesFromResponseBody(data: ILabel[]): ILabel[] {
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
      query: this.currentSearch(),
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.labelService.labelsParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState, currentSearch?: string): void {
    const queryParamsObj = {
      search: currentSearch,
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
