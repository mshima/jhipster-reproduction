import { Component, input } from '@angular/core';
import { KeyValuePipe, DecimalPipe } from '@angular/common';
import { TranslateDirective } from 'app/shared/language';

import { NgbProgressbar } from '@ng-bootstrap/ng-bootstrap/progressbar';
import { HttpServerRequests } from 'app/admin/metrics/metrics.model';
import { filterNaN } from 'app/core/util/operators';

@Component({
  selector: 'jhi-metrics-request',
  templateUrl: './metrics-request.html',
  imports: [NgbProgressbar, KeyValuePipe, DecimalPipe, TranslateDirective],
})
export class MetricsRequest {
  /**
   * Object containing http request related metrics
   */
  readonly requestMetrics = input<HttpServerRequests>();

  /**
   * Boolean field saying if the metrics are in the process of being updated
   */
  readonly updating = input<boolean>();

  filterNaN = filterNaN;
}
