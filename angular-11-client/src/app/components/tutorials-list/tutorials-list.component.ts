import { Component, OnInit } from '@angular/core';
import { Tutorial } from 'src/app/models/tutorial.model';
import { ConfigStateService } from 'src/app/services/config-state.service';
import { TutorialLockalService } from 'src/app/services/lockal-tutorial.service';
import { TutorialService } from 'src/app/services/tutorial.service';

@Component({
  selector: 'app-tutorials-list',
  templateUrl: './tutorials-list.component.html',
  styleUrls: ['./tutorials-list.component.css']
})
export class TutorialsListComponent implements OnInit {
  tutorials?: Tutorial[];
  currentTutorial?: Tutorial;
  currentIndex = -1;
  title = '';

  constructor(
    private tutorialLockalService: TutorialLockalService,
    private tutorialService: TutorialService,
    public config: ConfigStateService
  ) { }

  ngOnInit(): void {
    this.retrieveTutorials();
  }

  retrieveTutorials(): void {
    if (this.config.storeConfig.inSystem) {
      this.tutorialService.getAll()
        .subscribe(data => {
            this.tutorials = data;
            console.log(data);
            console.log(this.tutorials);
          },
          error => {
            console.log(error);
          });
    } else {
      this.tutorials = this.tutorialLockalService.getAll();
    }
  }

  refreshList(): void {
    this.retrieveTutorials();
    this.currentTutorial = undefined;
    this.currentIndex = -1;
  }

  setActiveTutorial(tutorial: any, index: number): void {
    this.currentTutorial = new Tutorial();
    this.currentTutorial.id = tutorial.taskId;
    this.currentTutorial.title = tutorial.title;
    this.currentTutorial.description = tutorial.description;
    this.currentTutorial.published = tutorial.published;
    this.currentTutorial.done = tutorial.done;

    this.currentIndex = index

    console.log(this.currentTutorial);
  }

  setActiveTutorialLocal(tutorial: any, index: number): void {
    this.currentTutorial = tutorial;
    this.currentIndex = index;
  }

  searchTitle(): void {
    this.currentTutorial = undefined;
    this.currentIndex = -1;

    this.tutorialService.findByTitle(this.title)
      .subscribe(
        data => {
          this.tutorials = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }
}
