import { TestBed } from '@angular/core/testing';

import { ConfiguratorApi } from './configurator-api';

describe('ConfiguratorApi', () => {
  let service: ConfiguratorApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfiguratorApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
