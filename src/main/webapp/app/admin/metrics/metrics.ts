import { Component, OnInit, inject, signal } from '@angular/core';
import { combineLatest } from 'rxjs';
import { TranslateDirective } from 'app/shared/language';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MetricsService } from './metrics.service';
import { MetricsModel, Thread } from './metrics.model';
import { JvmMemory } from './blocks/jvm-memory/jvm-memory';
import { JvmThreads } from './blocks/jvm-threads/jvm-threads';
import { MetricsCache } from './blocks/metrics-cache/metrics-cache';
import { MetricsDatasource } from './blocks/metrics-datasource/metrics-datasource';
import { MetricsEndpointsRequests } from './blocks/metrics-endpoints-requests/metrics-endpoints-requests';
import { MetricsGarbageCollector } from './blocks/metrics-garbagecollector/metrics-garbagecollector';
import { MetricsRequest } from './blocks/metrics-request/metrics-request';
import { MetricsSystem } from './blocks/metrics-system/metrics-system';

@Component({
  selector: 'jhi-metrics',
  templateUrl: './metrics.html',
  imports: [
    TranslateDirective,
    FontAwesomeModule,
    JvmMemory,
    JvmThreads,
    MetricsCache,
    MetricsDatasource,
    MetricsEndpointsRequests,
    MetricsGarbageCollector,
    MetricsRequest,
    MetricsSystem,
  ],
})
export default class Metrics implements OnInit {
  readonly metrics = signal<MetricsModel | undefined>(undefined);
  readonly threads = signal<Thread[] | undefined>(undefined);
  readonly updatingMetrics = signal(true);

  private readonly metricsService = inject(MetricsService);

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.updatingMetrics.set(true);
    combineLatest([this.metricsService.getMetrics(), this.metricsService.threadDump()]).subscribe(([metrics, threadDump]) => {
      this.metrics.set(metrics);
      this.threads.set(threadDump.threads);
      this.updatingMetrics.set(false);
    });
  }

  metricsKeyExistsAndObjectNotEmpty(key: keyof MetricsModel): boolean {
    const value = this.metrics()?.[key];
    return Boolean(value && Object.keys(value).length > 0);
  }
}
