import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfiguratorWidget } from './configurator-widget';

describe('ConfiguratorWidget', () => {
  let component: ConfiguratorWidget;
  let fixture: ComponentFixture<ConfiguratorWidget>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfiguratorWidget],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfiguratorWidget);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
