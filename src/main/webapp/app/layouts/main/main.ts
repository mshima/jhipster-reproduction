import { Component, inject, OnInit, DOCUMENT, RendererFactory2, Renderer2 } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import Footer from '../footer/footer';
import PageRibbon from '../profiles/page-ribbon';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, Footer, PageRibbon],
})
export default class Main implements OnInit {
  private readonly renderer: Renderer2;
  private readonly htmlElement: HTMLElement;

  private readonly router = inject(Router);
  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);
  private readonly accountService = inject(AccountService);
  private readonly document = inject(DOCUMENT);
  private readonly translateService = inject(TranslateService);
  private readonly rootRenderer = inject(RendererFactory2);

  constructor() {
    this.htmlElement = this.document.documentElement;
    this.renderer = this.rootRenderer.createRenderer(this.htmlElement, null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(this.htmlElement, 'lang', langChangeEvent.lang);
    });
  }
}
