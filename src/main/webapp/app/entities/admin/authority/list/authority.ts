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
import { IAuthority } from '../authority.model';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { AuthorityService } from '../service/authority.service';
import { AuthorityDeleteDialog } from '../delete/authority-delete-dialog';

@Component({
  selector: 'jhi-authority',
  templateUrl: './authority.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class Authority implements OnInit {
  subscription: Subscription | null = null;
  readonly authorities = signal<IAuthority[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly authorityService = inject(AuthorityService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.authorityService.authoritiesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.authorities.set(this.fillComponentAttributesFromResponseBody([...this.authorityService.authorities()]));
    });
  }

  trackName = (item: IAuthority): string => this.authorityService.getAuthorityIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.authorities().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(authority: IAuthority): void {
    const modalRef = this.modalService.open(AuthorityDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.authority = authority;
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

  protected refineData(data: IAuthority[]): IAuthority[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IAuthority[]): IAuthority[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.authorityService.authoritiesParams.set(queryObject);
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
