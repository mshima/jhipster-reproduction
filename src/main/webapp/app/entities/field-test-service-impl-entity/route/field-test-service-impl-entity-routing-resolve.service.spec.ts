import { beforeEach, describe, expect, it, vi } from 'vitest';

import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { lastValueFrom, of, throwError } from 'rxjs';

import { IFieldTestServiceImplEntity } from '../field-test-service-impl-entity.model';
import { FieldTestServiceImplEntityService } from '../service/field-test-service-impl-entity.service';

import fieldTestServiceImplEntityResolve from './field-test-service-impl-entity-routing-resolve.service';

describe('FieldTestServiceImplEntity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: FieldTestServiceImplEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    vi.spyOn(mockRouter, 'navigate');
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(FieldTestServiceImplEntityService);
  });

  describe('resolve', () => {
    it('should return IFieldTestServiceImplEntity returned by find', async () => {
      // GIVEN
      service.find = vi.fn(id => of({ id }));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          fieldTestServiceImplEntityResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              // THEN
              expect(service.find).toHaveBeenCalledWith(123);
              expect(result).toEqual({ id: 123 });
              resolve();
            },
          });
        });
      });
    });

    it('should return null if id is not provided', async () => {
      // GIVEN
      service.find = vi.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          fieldTestServiceImplEntityResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              // THEN
              expect(service.find).not.toHaveBeenCalled();
              expect(result).toBeNull();
              resolve();
            },
          });
        });
      });
    });

    it('should route to 404 page if data not found in server', async () => {
      // GIVEN
      vi.spyOn(service, 'find').mockReturnValue(throwError(() => new HttpErrorResponse({ status: 404, statusText: 'Not Found' })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      await TestBed.runInInjectionContext(async () => {
        await expect(lastValueFrom(fieldTestServiceImplEntityResolve(mockActivatedRouteSnapshot))).rejects.toThrow(
          'no elements in sequence',
        );
        // THEN
        expect(service.find).toHaveBeenCalledWith(123);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });

    it('should route to error page if server returns an error other than 404', async () => {
      // GIVEN
      vi.spyOn(service, 'find').mockReturnValue(
        throwError(() => new HttpErrorResponse({ status: 500, statusText: 'Internal Server Error' })),
      );
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      await TestBed.runInInjectionContext(async () => {
        await expect(lastValueFrom(fieldTestServiceImplEntityResolve(mockActivatedRouteSnapshot))).rejects.toThrow(
          'no elements in sequence',
        );
        // THEN
        expect(service.find).toHaveBeenCalledWith(123);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['error']);
      });
    });
  });
});
