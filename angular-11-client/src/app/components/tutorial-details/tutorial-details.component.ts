import { Component, OnInit } from '@angular/core';
import { TutorialService } from 'src/app/services/tutorial.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Tutorial } from 'src/app/models/tutorial.model';
import { TutorialLockalService } from 'src/app/services/lockal-tutorial.service';
import { ConfigStateService } from 'src/app/services/config-state.service';

@Component({
  selector: 'app-tutorial-details',
  templateUrl: './tutorial-details.component.html',
  styleUrls: ['./tutorial-details.component.css']
})
export class TutorialDetailsComponent implements OnInit {
  currentTutorial: Tutorial = {
    title: '',
    description: '',
    published: false,
    id: ''
  };
  message = '';

  constructor(
    private tutorialLockalService: TutorialLockalService,
    private tutorialService: TutorialService,
    public config: ConfigStateService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.message = '';
    if (this.config.storeConfig.inSystem){
      this.getTutorial(this.route.snapshot.params.id);
    }
    else {
      this.getTutorialLocal(this.route.snapshot.params.id);
    }
  }

  getTutorial(id: string): void {
    this.tutorialService.get(id)
      .subscribe(
        data => {
          this.currentTutorial.title = data.title;
          this.currentTutorial.description = data.description;
          this.currentTutorial.published = data.published;
          this.currentTutorial.id = data.taskId;
          this.currentTutorial.done = data.done;
          console.log(data);
          console.log(this.currentTutorial);
        },
        error => {
          console.log(error);
        });
  }

  getTutorialLocal(id: string): void {
    if (this.config.storeConfig.inSystem) {
      this.tutorialService.get(id)
        .subscribe(
          data => {
            this.currentTutorial = data;
            console.log(data);
          },
          error => {
            console.log(error);
          }
        );
    } else {
      this.currentTutorial = this.tutorialLockalService.get(id);
    }
  }

  updatePublished(status: boolean): void {
    const data = {
      title: this.currentTutorial.title,
      description: this.currentTutorial.description,
      published: status
    };
    this.message = '';
    if (this.config.storeConfig.inSystem) {
      this.tutorialService.update(this.currentTutorial.id, data)
      .subscribe(
        response => {
          this.currentTutorial.published = status;
          console.log(response);
          this.message = response.message ? response.message : 'This tutorial was updated successfully!';
        },
        error => {
          console.log(error);
        });
    } else {
      this.tutorialLockalService.update(this.currentTutorial.id, data)
    }
    
  }

  updateTutorial(): void {
    this.message = '';
    if (this.config.storeConfig.inSystem) {
      this.tutorialService.update(this.currentTutorial.id, this.currentTutorial)
      .subscribe(
        response => {
          console.log(response);
          this.message = response.message ? response.message : 'This tutorial was updated successfully!';
        },
        error => {
          console.log(error);
        });
    } else {
      this.tutorialLockalService.update(this.currentTutorial.id, this.currentTutorial);
    }
    
  }

  deleteTutorial(): void {
    if (this.config.storeConfig.inSystem) {
      this.tutorialService.delete(this.currentTutorial.id)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate(['/tutorials']);
        },
        error => {
          console.log(error);
        });
    } else {
      this.tutorialLockalService.delete(this.currentTutorial.id);
      this.router.navigate(['/tutorials']);
    }
    
  }
}
