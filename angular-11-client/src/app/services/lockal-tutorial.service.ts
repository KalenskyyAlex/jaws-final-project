import { Injectable } from '@angular/core';
import { Tutorial } from '../models/tutorial.model';
import { nanoid } from 'nanoid';

@Injectable({
    providedIn: 'root'
})
export class TutorialLockalService {
    constructor() { }

    getAll(): Tutorial[] {
        const tutorials = localStorage.getItem('tutorials');
        return tutorials ? JSON.parse(tutorials) : [];
    }

    get(id: string): Tutorial | undefined {
        const tutorials = this.getAll();
        return tutorials.find(tutorial => tutorial.id === id);
    }

    create(data: Tutorial): void {
        let tutorials = this.getAll();
        const id = nanoid();
        const tutorialWithId: Tutorial = {
            ...data,
            id: id
        };
        tutorials.push(tutorialWithId);
        localStorage.setItem('tutorials', JSON.stringify(tutorials));
    }

    update(id: string, data: Tutorial): void {
        const tutorials = this.getAll();
        const index = tutorials.findIndex(tutorial => tutorial.id === id);
        if (index !== -1) {
        tutorials[index] = {
            ...data,
            id: id
        };
        localStorage.setItem('tutorials', JSON.stringify(tutorials));
        }
    }

    delete(id: string): void {
        let tutorials = this.getAll();
        tutorials = tutorials.filter(tutorial => tutorial.id !== id);
        localStorage.setItem('tutorials', JSON.stringify(tutorials));
    }

    findByTitle(title: string): Tutorial[] {
        const tutorials = this.getAll();
        return tutorials.filter(tutorial => tutorial.title?.toLowerCase().includes(title.toLowerCase()));
    }
}
