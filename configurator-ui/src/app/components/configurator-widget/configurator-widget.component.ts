import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ConfiguratorApiService,
  ConfiguratorResponse
} from '../../services/configurator-api.service';

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
  response = signal<ConfiguratorResponse | null>(null);
  debugResponse = signal('');

  selections: Record<string, string> = {};

  init(): void {
    this.loading.set(true);
    this.error.set('');

    this.api.initConfiguration({
      productId: 'DEMO_PRODUCT',
      kbId: 'KB_001',
      locale: 'de-DE',
      selections: this.selections
    }).subscribe({
      next: (res) => {
        this.response.set(res);
        this.debugResponse.set(JSON.stringify(res, null, 2));
        this.syncSelectionsFromResponse(res);
        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Init failed');
        this.loading.set(false);
      }
    });
  }

  update(): void {
    const current = this.response();
    if (!current?.configurationId) return;

    this.loading.set(true);
    this.error.set('');

    this.api.updateConfiguration({
      configurationId: current.configurationId,
      selections: this.selections
    }).subscribe({
      next: (res) => {
        this.response.set(res);
        this.debugResponse.set(JSON.stringify(res, null, 2));
        this.syncSelectionsFromResponse(res);
        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Update failed');
        this.loading.set(false);
      }
    });
  }

  onSelectionChange(attributeName: string, value: string): void {
    this.selections[attributeName] = value;
  }

  private syncSelectionsFromResponse(res: ConfiguratorResponse): void {
    for (const attr of res.attributes) {
      const selected = attr.values.find(v => v.selected);
      if (selected) {
        this.selections[attr.name] = selected.value;
      }
    }
  }
}