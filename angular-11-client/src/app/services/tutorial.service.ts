import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tutorial } from '../models/tutorial.model';
import { environment } from 'src/environments/environment';
import { ConfigStateService } from './config-state.service';

@Injectable({
  providedIn: 'root'
})
export class TutorialService {

  constructor(private http: HttpClient,
    public config: ConfigStateService) { }

  getAll(): Observable<Tutorial[]> {
    return this.http.get<Tutorial[]>(`${environment.endpoint}/users/${this.config.storeConfig.userHash}`);
  }

  get(id: any): Observable<Tutorial> {
    return this.http.get(`${environment.endpoint}/${this.config.storeConfig.userHash}/${id}`);
  }

  create(data: any): Observable<any> {
    return this.http.post(`${environment.endpoint}/${this.config.storeConfig.userHash}`, data);
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${environment.endpoint}/${this.config.storeConfig.userHash}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${environment.endpoint}/${this.config.storeConfig.userHash}/${id}`);
  }

  findByTitle(title: any): Observable<Tutorial[]> {
    return this.http.get<Tutorial[]>(`${environment.endpoint}/${this.config.storeConfig.userHash}?title=${title}`);
  }
}
