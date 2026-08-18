import { Component, input } from '@angular/core';
import { KeyValuePipe, DecimalPipe } from '@angular/common';
import { TranslateDirective } from 'app/shared/language';

import { NgbProgressbar } from '@ng-bootstrap/ng-bootstrap/progressbar';
import { JvmMetrics } from 'app/admin/metrics/metrics.model';

@Component({
  selector: 'jhi-jvm-memory',
  templateUrl: './jvm-memory.html',
  imports: [NgbProgressbar, KeyValuePipe, DecimalPipe, TranslateDirective],
})
export class JvmMemory {
  /**
   * Object containing all jvm memory metrics
   */
  readonly jvmMemoryMetrics = input<Record<string, JvmMetrics>>();

  /**
   * Boolean field saying if the metrics are in the process of being updated
   */
  readonly updating = input<boolean>();
}
