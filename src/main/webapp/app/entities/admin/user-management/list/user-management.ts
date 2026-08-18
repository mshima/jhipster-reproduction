import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertError } from 'app/shared/alert/alert-error';
import { Alert } from 'app/shared/alert/alert';
import { TranslateDirective } from 'app/shared/language';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { SortDirective, SortByDirective, sortStateSignal, SortService, SortState } from 'app/shared/sort';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { SORT } from 'app/config/navigation.constants';
import { ItemCount } from 'app/shared/pagination';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from '../service/user-management.service';
import { IUserManagement } from '../user-management.model';
import { UserManagementDeleteDialog } from '../delete/user-management-delete-dialog';

@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.html',
  imports: [
    RouterLink,
    FontAwesomeModule,
    AlertError,
    Alert,
    NgbPagination,
    TranslateDirective,
    SortDirective,
    SortByDirective,
    ItemCount,
    DatePipe,
  ],
})
export class UserManagement implements OnInit {
  readonly currentAccount = inject(AccountService).account;
  readonly users = signal<IUserManagement[] | null>(null);
  readonly isLoading = signal(false);
  readonly totalItems = signal(0);
  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly page = signal(0);
  sortState = sortStateSignal({});

  private readonly userService = inject(UserManagementService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly sortService = inject(SortService);
  private readonly modalService = inject(NgbModal);

  ngOnInit(): void {
    this.handleNavigation();
  }

  setActive(userManagement: IUserManagement, isActivated: boolean): void {
    this.userService.update({ ...userManagement, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(item: IUserManagement): number {
    return item.id!;
  }

  deleteUser(userManagement: IUserManagement): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userManagement = userManagement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.userService
      .query({
        page: this.page() - 1,
        size: this.itemsPerPage(),
        sort: this.sortService.buildSortParam(this.sortState(), 'id'),
      })
      .subscribe({
        next: (res: HttpResponse<IUserManagement[]>) => {
          this.isLoading.set(false);
          this.onSuccess(res.body, res.headers);
        },
        error: () => this.isLoading.set(false),
      });
  }

  transition(sortState?: SortState): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page(),
        sort: this.sortService.buildSortParam(sortState ?? this.sortState()),
      },
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get(PAGE_HEADER);
      this.page.set(+(page ?? 1));
      this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data.defaultSort));
      this.loadAll();
    });
  }

  private onSuccess(users: IUserManagement[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
    this.users.set(users);
  }
}
