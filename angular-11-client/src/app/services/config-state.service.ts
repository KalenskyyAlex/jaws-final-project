import { Injectable } from '@angular/core';
import { ConfigState } from '../models/config.state';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConfigStateService {
  private subject = new BehaviorSubject<ConfigState>(new ConfigState());
  state = this.subject.asObservable();

  storeConfig = new ConfigState(); // this is the variable you will use

  constructor() {
    this.state.subscribe((state) => (this.storeConfig = state));
  }
}