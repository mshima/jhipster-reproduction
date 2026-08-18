import { MockInstance, afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faEye, faPencilAlt, faPlus, faSort, faSortDown, faSortUp, faSync, faTimes } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of, Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { sampleWithRequiredData } from '../field-test-infinite-scroll-entity.test-samples';
import { FieldTestInfiniteScrollEntityService } from '../service/field-test-infinite-scroll-entity.service';

import { FieldTestInfiniteScrollEntity } from './field-test-infinite-scroll-entity';

vi.useFakeTimers();

describe('FieldTestInfiniteScrollEntity Management Component', () => {
  let httpMock: HttpTestingController;
  let comp: FieldTestInfiniteScrollEntity;
  let fixture: ComponentFixture<FieldTestInfiniteScrollEntity>;
  let service: FieldTestInfiniteScrollEntityService;
  let routerNavigateSpy: MockInstance;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: {
              queryParams: {},
              queryParamMap: convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            },
          },
        },
      ],
    });

    fixture = TestBed.createComponent(FieldTestInfiniteScrollEntity);
    comp = fixture.componentInstance;
    service = TestBed.inject(FieldTestInfiniteScrollEntityService);
    routerNavigateSpy = vi.spyOn(comp.router, 'navigate');

    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faEye, faPencilAlt, faPlus, faSort, faSortDown, faSortUp, faSync, faTimes);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    TestBed.resetTestingModule();
    httpMock.verify();
  });

  it('should call load all on init', async () => {
    // WHEN
    TestBed.tick();
    const req = httpMock.expectOne({ method: 'GET' });
    req.flush([{ id: 20874 }], { headers: { link: '<http://localhost/api/foo?page=1&size=20>; rel="next"' } });
    await vi.runAllTimersAsync();

    // THEN
    expect(comp.isLoading()).toEqual(false);
    expect(comp.fieldTestInfiniteScrollEntities()[0]).toEqual(expect.objectContaining({ id: 20874 }));
  });

  describe('trackId', () => {
    it('should forward to fieldTestInfiniteScrollEntityService', () => {
      const entity = { id: 20874 };
      vi.spyOn(service, 'getFieldTestInfiniteScrollEntityIdentifier');
      const id = comp.trackId(entity);
      expect(service.getFieldTestInfiniteScrollEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // WHEN
    comp.navigateToWithComponentValues({ predicate: 'non-existing-column', order: 'asc' });

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['non-existing-column,asc'],
        }),
      }),
    );
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    TestBed.tick();
    httpMock.expectOne({ method: 'GET' });

    // THEN
    expect(service.fieldTestInfiniteScrollEntitiesParams()).toMatchObject({ sort: ['id,desc'] });
  });

  it('should infinite scroll', async () => {
    // GIVEN
    TestBed.tick();
    let req = httpMock.expectOne({ method: 'GET' });
    req.flush([{ id: 20874 }], { headers: { link: '<http://localhost/api/foo?page=1&size=20>; rel="next"' } });
    await vi.runAllTimersAsync();
    expect(comp.fieldTestInfiniteScrollEntities()).toHaveLength(1);
    expect(comp.fieldTestInfiniteScrollEntities()[0]).toEqual(expect.objectContaining({ id: 20874 }));

    // WHEN
    comp.loadNextPage();
    TestBed.tick();
    expect(service.fieldTestInfiniteScrollEntitiesParams()).toMatchObject({ page: '1' });
    req = httpMock.expectOne({ method: 'GET' });
    req.flush([{ id: 12964 }], {
      headers: { link: '<http://localhost/api/foo?page=0&size=20>; rel="prev",<http://localhost/api/foo?page=2&size=20>; rel="next"' },
    });
    await vi.runAllTimersAsync();
    expect(comp.fieldTestInfiniteScrollEntities()).toHaveLength(2);
    expect(comp.fieldTestInfiniteScrollEntities()[1]).toEqual(expect.objectContaining({ id: 12964 }));

    comp.loadNextPage();
    TestBed.tick();
    expect(service.fieldTestInfiniteScrollEntitiesParams()).toMatchObject({ page: '2' });
    req = httpMock.expectOne({ method: 'GET' });
    req.flush([{ id: 12964 }], {
      headers: { link: '<http://localhost/api/foo?page=0&size=20>; rel="prev",<http://localhost/api/foo?page=2&size=20>; rel="next"' },
    });
    await vi.runAllTimersAsync();
    expect(comp.fieldTestInfiniteScrollEntities()).toHaveLength(2);
    expect(comp.fieldTestInfiniteScrollEntities()[1]).toEqual(expect.objectContaining({ id: 12964 }));
  });

  describe('delete', () => {
    let ngbModal: NgbModal;
    let deleteModalMock: any;

    beforeEach(() => {
      deleteModalMock = { componentInstance: {}, closed: new Subject() };
      // NgbModal is not a singleton using TestBed.inject.
      // ngbModal = TestBed.inject(NgbModal);
      ngbModal = (comp as unknown as { modalService: NgbModal }).modalService;
      vi.spyOn(ngbModal, 'open').mockReturnValue(deleteModalMock);
    });

    it('on confirm should call load', () => {
      // GIVEN
      vi.spyOn(comp, 'load');

      // WHEN
      comp.delete(sampleWithRequiredData);
      deleteModalMock.closed.next('deleted');

      // THEN
      expect(ngbModal.open).toHaveBeenCalled();
      expect(comp.load).toHaveBeenCalled();
    });

    it('on dismiss should call load', () => {
      // GIVEN
      vi.spyOn(comp, 'load');

      // WHEN
      comp.delete(sampleWithRequiredData);
      deleteModalMock.closed.next();

      // THEN
      expect(ngbModal.open).toHaveBeenCalled();
      expect(comp.load).not.toHaveBeenCalled();
    });
  });
});
