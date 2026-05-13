import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface InitConfigurationRequest {
  productId: string;
  kbId: string;
  locale: string;
  selections: Record<string, string>;
}

export interface UpdateConfigurationRequest {
  configurationId: string;
  selections: Record<string, string>;
}

export interface ConfiguratorValue {
  value: string;
  label: string;
  selectable: boolean;
  selected: boolean;
}

export interface ConfiguratorAttribute {
  name: string;
  label: string;
  values: ConfiguratorValue[];
}

export interface ConfiguratorResponse {
  configurationId: string;
  productId: string;
  attributes: ConfiguratorAttribute[];
  price: {
    amount: number;
    currency: string;
  };
  complete: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ConfiguratorApiService {
  private http = inject(HttpClient);

  health(): Observable<string> {
    return this.http.get('/api/health', { responseType: 'text' });
  }

  initConfiguration(payload: InitConfigurationRequest): Observable<ConfiguratorResponse> {
    return this.http.post<ConfiguratorResponse>('/api/configurations/init', payload);
  }

  updateConfiguration(payload: UpdateConfigurationRequest): Observable<ConfiguratorResponse> {
    return this.http.post<ConfiguratorResponse>('/api/configurations/update', payload);
  }
}