import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { LoginService } from 'src/app/services/login.service';
import { ConfigStateService } from 'src/app/services/config-state.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = {
    name: '',
    password: '',
  };

  loginStatus = null;

  constructor(private tutorialService: LoginService,
    public config: ConfigStateService, private router: Router) { }

  ngOnInit(): void {
  }

  submitLogin(): void {
    const data = {
      name: this.user.name,
      password: this.user.password
    };

    this.tutorialService.validateLogin(data)
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