import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  // return:
  // 0 - Invalid Data
  // 2 - No Such User
  // 3 - Invalid Password
  // 4 - Success
  validateLogin(data: any): Observable<any> {
    return this.http.post('/auth/login', data);
  }

  // return:
  // 0 - Invalid Data
  // 1 - User Exists
  // 3 - Invalid Password
  // 4 - Success
  validateRegister(data: any): Observable<any> {
    return this.http.post('/auth/registration', data);
  }
}
