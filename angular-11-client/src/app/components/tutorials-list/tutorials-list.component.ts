import { Component, OnInit } from '@angular/core';
import { Tutorial } from 'src/app/models/tutorial.model';
import { ConfigStateService } from 'src/app/services/config-state.service';
import { TutorialService } from 'src/app/services/tutorial.service';

@Component({
  selector: 'app-tutorials-list',
  templateUrl: './tutorials-list.component.html',
  styleUrls: ['./tutorials-list.component.css']
})
export class TutorialsListComponent implements OnInit {
  tutorials?: Tutorial[];
  currentTutorial?: any;
  currentIndex = -1;
  title = '';

  constructor(private tutorialService: TutorialService,
    public config: ConfigStateService) { }

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
      console.log("not in system")
      this.tutorialService.getAllLocal()
    }
  }

  refreshList(): void {
    this.retrieveTutorials();
    this.currentTutorial = undefined;
    this.currentIndex = -1;
  }

  setActiveTutorial(tutorial: any, index: number): void {
    alert(tutorial.id);
    console.log(tutorial);

    this.currentTutorial.id = tutorial.taskId;
    this.currentTutorial.title = tutorial.title;
    this.currentTutorial.description = tutorial.description;
    this.currentTutorial.published = tutorial.published;
    this.currentTutorial.done = tutorial.done;

    this.currentIndex = index
    console.log(this.currentTutorial);
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
