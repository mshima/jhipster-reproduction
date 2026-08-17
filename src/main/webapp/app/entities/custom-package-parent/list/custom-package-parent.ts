import { Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ICustomPackageParent } from '../custom-package-parent.model';
import { CustomPackageParentDeleteDialog } from '../delete/custom-package-parent-delete-dialog';
import { CustomPackageParentService } from '../service/custom-package-parent.service';

@Component({
  selector: 'jhi-custom-package-parent',
  templateUrl: './custom-package-parent.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class CustomPackageParent implements OnInit {
  subscription: Subscription | null = null;
  readonly customPackageParents = signal<ICustomPackageParent[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly customPackageParentService = inject(CustomPackageParentService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.customPackageParentService.customPackageParentsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.customPackageParents.set(
        this.fillComponentAttributesFromResponseBody([...this.customPackageParentService.customPackageParents()]),
      );
    });
  }

  trackId = (item: ICustomPackageParent): number => this.customPackageParentService.getCustomPackageParentIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.customPackageParents().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(customPackageParent: ICustomPackageParent): void {
    const modalRef = this.modalService.open(CustomPackageParentDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customPackageParent = customPackageParent;
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

  protected refineData(data: ICustomPackageParent[]): ICustomPackageParent[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ICustomPackageParent[]): ICustomPackageParent[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.customPackageParentService.customPackageParentsParams.set(queryObject);
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
