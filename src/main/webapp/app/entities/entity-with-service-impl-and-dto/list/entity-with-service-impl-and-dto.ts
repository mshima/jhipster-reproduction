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
import { IEntityWithServiceImplAndDTO } from '../entity-with-service-impl-and-dto.model';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityWithServiceImplAndDTOService } from '../service/entity-with-service-impl-and-dto.service';
import { EntityWithServiceImplAndDTODeleteDialog } from '../delete/entity-with-service-impl-and-dto-delete-dialog';

@Component({
  selector: 'jhi-entity-with-service-impl-and-dto',
  templateUrl: './entity-with-service-impl-and-dto.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class EntityWithServiceImplAndDTO implements OnInit {
  subscription: Subscription | null = null;
  readonly entityWithServiceImplAndDTOS = signal<IEntityWithServiceImplAndDTO[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly entityWithServiceImplAndDTOService = inject(EntityWithServiceImplAndDTOService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.entityWithServiceImplAndDTOService.entityWithServiceImplAndDTOSResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.entityWithServiceImplAndDTOS.set(
        this.fillComponentAttributesFromResponseBody([...this.entityWithServiceImplAndDTOService.entityWithServiceImplAndDTOS()]),
      );
    });
  }

  trackId = (item: IEntityWithServiceImplAndDTO): number =>
    this.entityWithServiceImplAndDTOService.getEntityWithServiceImplAndDTOIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.entityWithServiceImplAndDTOS().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(entityWithServiceImplAndDTO: IEntityWithServiceImplAndDTO): void {
    const modalRef = this.modalService.open(EntityWithServiceImplAndDTODeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entityWithServiceImplAndDTO = entityWithServiceImplAndDTO;
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

  protected refineData(data: IEntityWithServiceImplAndDTO[]): IEntityWithServiceImplAndDTO[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IEntityWithServiceImplAndDTO[]): IEntityWithServiceImplAndDTO[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.entityWithServiceImplAndDTOService.entityWithServiceImplAndDTOSParams.set(queryObject);
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
