import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { ConfigStateService } from 'src/app/services/config-state.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  
  user: User = {
    name: '',
    email: '',
    password: '',
  };

  loginStatus = null;

  constructor(private tutorialService: LoginService,
    public config: ConfigStateService, private router: Router) { }

  ngOnInit(): void {
  }

  submitRegister(): void {
    const data = {
      name: this.user.name,
      email: this.user.email,
      password: this.user.password
    };

    this.tutorialService.validateRegister(data)
      .subscribe(
        response => {
          console.log(response);
          this.loginStatus = response;
        },
        error => {
          console.log(error);
        }
    )

    if (this.loginStatus === 4) {
      this.tutorialService.getUserHash(this.user.name)
        .subscribe(
          response => {
            console.log(response);
            this.config.storeConfig.userHash = response.userName;
          },
          error => {
            console.log(error);
          }
        )

      if (this.config.storeConfig.userHash === "") {
        this.loginStatus = null;
        return;
      }
      
      this.config.storeConfig.inSystem = true;
    }
  }
  
}
