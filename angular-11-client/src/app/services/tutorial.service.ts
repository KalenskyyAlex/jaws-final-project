import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tutorial } from '../models/tutorial.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TutorialService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Tutorial[]> {
    return this.http.get<Tutorial[]>(environment.endpoint);
  }

  get(id: any): Observable<Tutorial> {
    return this.http.get(`${environment.endpoint}/${id}`);
  }

  create(data: any): Observable<any> {
    return this.http.post(environment.endpoint, data);
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${environment.endpoint}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${environment.endpoint}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(environment.endpoint);
  }

  findByTitle(title: any): Observable<Tutorial[]> {
    return this.http.get<Tutorial[]>(`${environment.endpoint}?title=${title}`);
  }
}
