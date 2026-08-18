import { Component, input } from '@angular/core';
import { KeyValuePipe, DecimalPipe } from '@angular/common';
import { TranslateDirective } from 'app/shared/language';

import { CacheMetrics } from 'app/admin/metrics/metrics.model';
import { filterNaN } from 'app/core/util/operators';

@Component({
  selector: 'jhi-metrics-cache',
  templateUrl: './metrics-cache.html',
  imports: [KeyValuePipe, DecimalPipe, TranslateDirective],
})
export class MetricsCache {
  /**
   * Object containing all cache related metrics
   */
  readonly cacheMetrics = input<Record<string, CacheMetrics>>();

  /**
   * Boolean field saying if the metrics are in the process of being updated
   */
  readonly updating = input<boolean>();

  filterNaN = filterNaN;
}
