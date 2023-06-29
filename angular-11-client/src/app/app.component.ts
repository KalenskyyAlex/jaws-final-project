import { Component } from '@angular/core';
import { ConfigStateService } from './services/config-state.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'jAWS-tutorial';

  constructor(public config: ConfigStateService) { };
}