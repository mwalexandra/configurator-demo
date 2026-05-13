import { Component, OnInit, inject } from '@angular/core';
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
export class ConfiguratorWidgetComponent implements OnInit {
  private api = inject(ConfiguratorApiService);

  response?: ConfiguratorResponse;
  loading = false;
  error = '';

  selections: Record<string, string> = {
    COLOR: 'RED',
    ENGINE: 'ELECTRIC'
  };

  ngOnInit(): void {
    this.init();
  }

  init(): void {
    this.loading = true;
    this.error = '';

    this.api.initConfiguration({
      productId: 'DEMO_PRODUCT',
      kbId: 'KB_001',
      locale: 'de-DE',
      selections: this.selections
    }).subscribe({
      next: (res) => {
        this.response = res;
        this.syncSelectionsFromResponse();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Init request failed';
        console.error(err);
        this.loading = false;
      }
    });
  }

  onSelectionChange(attributeName: string, value: string): void {
    this.selections[attributeName] = value;
  }

  update(): void {
    if (!this.response?.configurationId) return;

    this.loading = true;
    this.error = '';

    this.api.updateConfiguration({
      configurationId: this.response.configurationId,
      selections: this.selections
    }).subscribe({
      next: (res) => {
        this.response = res;
        this.syncSelectionsFromResponse();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Init failed: ' + err.message;
        this.loading = false;
        console.error(err);
      }
    });
  }

  trackByAttribute(_: number, item: any): string {
    return item.name;
  }

  private syncSelectionsFromResponse(): void {
    if (!this.response) return;

    for (const attribute of this.response.attributes) {
      const selected = attribute.values.find(v => v.selected);
      if (selected) {
        this.selections[attribute.name] = selected.value;
      }
    }
  }
}