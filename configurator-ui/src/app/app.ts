import { Component } from '@angular/core';
import { ConfiguratorWidgetComponent } from './components/configurator-widget/configurator-widget.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ConfiguratorWidgetComponent],
  template: `<app-configurator-widget />`
})
export class App {}