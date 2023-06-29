import { Component } from '@angular/core';
import { ConfigStateService } from './services/config-state.service';
import { Route, Router } from '@angular/router';
import { TutorialsListComponent } from './components/tutorials-list/tutorials-list.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'jAWS-tutorial';

  constructor(public config: ConfigStateService,
              private router: Router,
              private tutorialListComponent: TutorialsListComponent) { };
  
  logOut(): void {
    this.config.storeConfig.inSystem = false;
    this.config.storeConfig.userHash = "";

    this.tutorialListComponent.refreshList();
  }
}