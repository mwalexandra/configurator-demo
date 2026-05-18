import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConfiguratorApiService } from '../../services/configurator-api.service';

@Component({
  selector: 'app-configurator-widget',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './configurator-widget.component.html',
  styleUrl: './configurator-widget.component.scss'
})
export class ConfiguratorWidgetComponent {
  private api = inject(ConfiguratorApiService);

  loading = signal(false);
  error = signal('');
  debugResponse = signal('');

  selections: Record<string, string> = {};

  init(): void {
    console.log('init() start');
    this.loading.set(true);
    this.error.set('');

    this.api.initConfiguration({
      productId: 'DEMO_PRODUCT',
      kbId: 'KB_001',
      locale: 'de-DE',
      selections: this.selections
    }).subscribe({
      next: (res) => {
        console.log('response arrived', res);
        this.debugResponse.set(JSON.stringify(res, null, 2));
        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Init failed');
        this.loading.set(false);
      }
    });
  }
}