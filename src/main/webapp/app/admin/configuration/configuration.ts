import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KeyValuePipe, JsonPipe } from '@angular/common';
import { TranslateDirective } from 'app/shared/language';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SortDirective, SortByDirective, sortStateSignal, SortService } from 'app/shared/sort';
import { ConfigurationService } from './configuration.service';
import { Bean, PropertySource } from './configuration.model';

@Component({
  selector: 'jhi-configuration',
  templateUrl: './configuration.html',
  imports: [FontAwesomeModule, FormsModule, SortDirective, SortByDirective, KeyValuePipe, JsonPipe, TranslateDirective],
})
export default class Configuration implements OnInit {
  readonly allBeans = signal<Bean[] | undefined>(undefined);
  readonly beansFilter = signal('');
  readonly propertySources = signal<PropertySource[]>([]);
  sortState = sortStateSignal({ predicate: 'prefix', order: 'asc' });
  readonly beans = computed(() => {
    let data = this.allBeans() ?? [];
    const beansFilter = this.beansFilter();
    if (beansFilter) {
      data = data.filter(bean => bean.prefix.toLowerCase().includes(beansFilter.toLowerCase()));
    }

    const { order, predicate } = this.sortState();
    if (predicate && order) {
      data = data.sort(this.sortService.startSort({ predicate, order }));
    }
    return data;
  });

  private readonly sortService = inject(SortService);
  private readonly configurationService = inject(ConfigurationService);

  ngOnInit(): void {
    this.configurationService.getBeans().subscribe(beans => {
      this.allBeans.set(beans);
    });

    this.configurationService.getPropertySources().subscribe(propertySources => this.propertySources.set(propertySources));
  }
}
