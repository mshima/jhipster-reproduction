import { Component, input } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { TranslateDirective } from 'app/shared/language';

import { Databases } from 'app/admin/metrics/metrics.model';
import { filterNaN } from 'app/core/util/operators';

@Component({
  selector: 'jhi-metrics-datasource',
  templateUrl: './metrics-datasource.html',
  imports: [DecimalPipe, TranslateDirective],
})
export class MetricsDatasource {
  /**
   * Object containing all datasource related metrics
   */
  readonly datasourceMetrics = input<Databases>();

  /**
   * Boolean field saying if the metrics are in the process of being updated
   */
  readonly updating = input<boolean>();

  filterNaN = filterNaN;
}
